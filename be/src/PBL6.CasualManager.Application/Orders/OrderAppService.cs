using Microsoft.AspNetCore.Mvc;
using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.Oders;
using PBL6.CasualManager.PrieceDetails;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.WorkerInfos;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;
using Volo.Abp.Identity;

namespace PBL6.CasualManager.Orders
{
    public class OrderAppService :
        CrudAppService<
            Order,
            OrderDto,
            Guid,
            OrderConditionSearchDto,
            OrderCreateUpdateDto>,
        IOrderAppService
    {
        private readonly IOrderRepository _orderRepository;
        private readonly IIdentityUserRepository _identityUserRepository;
        private readonly IWorkerInfoRepository _workerInfoRepository;
        private readonly ICustomerInfoRepository _customerInfoRepository;
        private readonly IPrieceDetailRepository _prieceDetailRepository;
        private readonly ITypeOfJobRepository _typeOfJobRepository;
        private readonly IJobInfoRepository _jobInfoRepository;

        public OrderAppService(
            IOrderRepository orderRepository, IJobInfoRepository jobInfoRepository,
            IIdentityUserRepository identityUserRepository, IWorkerInfoRepository workerInfoRepository,
            ICustomerInfoRepository customerInfoRepository, IPrieceDetailRepository prieceDetailRepository,
            ITypeOfJobRepository typeOfJobRepository) : base(orderRepository)
        {
            _orderRepository = orderRepository;
            _identityUserRepository = identityUserRepository;
            _jobInfoRepository = jobInfoRepository;
            _customerInfoRepository = customerInfoRepository;
            _workerInfoRepository = workerInfoRepository;
            _prieceDetailRepository = prieceDetailRepository;
            _typeOfJobRepository = typeOfJobRepository;
        }

        public async Task<PagedResultDto<OrderDto>> GetListByWorkerAsync(OrderConditionSearchDto condition)
        {
            var result = await _orderRepository.GetListByUserAsync(
                condition.SkipCount,
                condition.MaxResultCount,
                condition.Sorting,
                condition.CustomerId,
                condition.WorkerId);
            var listOrderDto = new List<OrderDto>();
            foreach(var item in result.Items)
            {
                var orderDto = ObjectMapper.Map<Order, OrderDto>(item);
                var identityUser = await _identityUserRepository.GetAsync(item.CustomerInfo.UserId);
                orderDto.CustomerName = identityUser.Name;
                listOrderDto.Add(orderDto);
            }
            return new PagedResultDto<OrderDto> { Items = listOrderDto, TotalCount = result.TotalCount };
        }

        public async Task<PagedResultDto<OrderDto>> GetListByCustomerAsync(OrderConditionSearchDto condition)
        {
            var result = await _orderRepository.GetListByUserAsync(
                condition.SkipCount,
                condition.MaxResultCount,
                condition.Sorting,
                condition.CustomerId,
                condition.WorkerId);
            var listOrderDto = new List<OrderDto>();
            foreach (var item in result.Items)
            {
                var orderDto = ObjectMapper.Map<Order, OrderDto>(item);
                var identityUser = await _identityUserRepository.GetAsync(item.WorkerInfo.UserId);
                orderDto.WorkerName = identityUser.Name;
                listOrderDto.Add(orderDto);
            }
            return new PagedResultDto<OrderDto> { Items = listOrderDto, TotalCount = result.TotalCount };
        }

        public async Task<Dictionary<string, string>> GetListRevenueOfMonthsOfWorkerAsync(Guid workerId, int month = 1)
        {
            var result = await _orderRepository.GetListRevenueOfMonthsOfWorker(workerId, month);
            return result;
        }

        public async Task<Dictionary<string, Dictionary<string, string>>> GetListOrderOfMonthsOfWorkerAsync(Guid workerId, int month = 1)
        {
            var result = await _orderRepository.GetListOrderOfMonthsOfWorker(workerId, month);
            return result;
        }

        public async Task<ApiResult<List<OrderOfCustomerResponse>>> GetListHistoryOrderOfCustomer(Guid idCustomer)
        {
            try
            {
                var listOrder = await _orderRepository.GetListAsync(x => x.CustomerInfo.UserId == idCustomer, includeDetails: true);
                var listJobInfo = await _jobInfoRepository.GetListAsync();
                var listWorkerInfo = await _workerInfoRepository.GetListAsync();
                var listUser = await _identityUserRepository.GetListAsync();
                var result = (from order in listOrder
                              join jobInfo in listJobInfo on order.JobInfoId equals jobInfo.Id
                              join workerInfo in listWorkerInfo on order.WorkerId equals workerInfo.Id
                              join identityUser in listUser on workerInfo.UserId equals identityUser.Id
                              where order.Status == Enum.OrderStatus.IsComplete
                              orderby order.CreationTime descending
                              select new OrderOfCustomerResponse()
                              {
                                  Id = order.Id,
                                  CreationTime = order.CreationTime.ToString("dd-MM-yyyy"),
                                  JobInfoId = jobInfo.Id,
                                  JobInfoName = jobInfo.Name,
                                  Note = order.Note,
                                  WorkerId = order.WorkerId,
                                  Price = order.JobPrices.ToString(),
                                  StatusPaid = order.IsPaid.ToString(),
                                  TimeCompletion = order.LastModificationTime?.ToString("dd-MM-yyyy"),
                                  WorkerName = identityUser.Name,
                              }).ToList();
                return new ApiSuccessResult<List<OrderOfCustomerResponse>>(resultObj: result);
            }
            catch (Exception)
            {
                return new ApiErrorResult<List<OrderOfCustomerResponse>>(message: "Đã có lỗi trong quá trình lấy dữ liệu");
            }
        }

        public async Task<ApiResult<List<OrderOfWokerForChartResponse>>> GetListHistoryOrderOfWorkerSixMonth(Guid idWorker)
        {
            try
            {
                var listOrderOfWokerForChartResponse = new List<OrderOfWokerForChartResponse>();
                var listOrder = await _orderRepository.GetListAsync(x => x.WorkerInfo.UserId == idWorker, includeDetails: true);
                DateTime today = DateTime.Now;
                var listPrieceDetail = await _prieceDetailRepository.GetListAsync();
                int countLoop = 5;// loop 6 times equivalent 6 months
                while (countLoop >= 0)
                {
                    var ordersInMonth = listOrder.Where(x => x.CreationTime.Month == today.Month);
                    var order_prieces = (from order in ordersInMonth
                                         join prieceDetail in listPrieceDetail on order.PrieceDetailId equals prieceDetail.Id
                                         into orderPriceList
                                         from orderPrice in orderPriceList.DefaultIfEmpty()
                                         select new { orderPrice }).ToList();
                    var orderOfWokerForChartResponse = new OrderOfWokerForChartResponse()
                    {
                        Month = today.Month.ToString(),
                        Revenue = order_prieces.Sum(x => x.orderPrice.FeeForWorker).ToString(),
                        TotalOrder = order_prieces.Count().ToString()
                    };
                    listOrderOfWokerForChartResponse.Add(orderOfWokerForChartResponse);
                    today = today.AddMonths(-1);
                    countLoop--;
                }
                listOrderOfWokerForChartResponse = listOrderOfWokerForChartResponse.OrderBy(x => int.Parse(x.Month)).ToList();
                return new ApiSuccessResult<List<OrderOfWokerForChartResponse>>(resultObj: listOrderOfWokerForChartResponse);
            }
            catch (Exception)
            {
                return new ApiErrorResult<List<OrderOfWokerForChartResponse>>(message: "Có lỗi trong quá trình lấy dữ liệu!");
            }
        }

        [HttpPost]
        [Route("api/app/order/{idCustomer}")]
        public async Task<ApiResult<OrderResponse>> CreateOrder(Guid idCustomer, CreateOrderRequest request)
        {
            try
            {
                var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(request.WorkerId);
                if (workerInfo == null)
                {
                    return new ApiErrorResult<OrderResponse>(message: "Thợ không tồn tại!");
                }
                if (workerInfo.Status == Enum.WorkerStatus.Busy)
                {
                    return new ApiErrorResult<OrderResponse>(message: "Thợ đang bận! Không thể đặt đơn!");
                }
                var priceDetail = new PrieceDetail()
                {
                    FeeForWorker = 0,
                    FeeForBussiness = 0
                };
                var objectPriceDetail = await _prieceDetailRepository.InsertAsync(priceDetail, true);
                if (objectPriceDetail == null)
                {
                    return new ApiErrorResult<OrderResponse>(message: "Có lỗi trong quá trình đặt đơn!");
                }
                var wokerIdentityInfo = await _identityUserRepository.GetAsync(request.WorkerId);
                var customerIdentityInfo = await _identityUserRepository.GetAsync(idCustomer);
                var jobInfo = await _jobInfoRepository.GetAsync(request.JobInfoId);
                var customerInfo = await _customerInfoRepository.GetEntityCustomerInfoHaveUserId(customerIdentityInfo.Id);
                var order = new Order()
                {
                    WorkerId = workerInfo.Id,
                    IsPaid = false,
                    JobInfoId = request.JobInfoId,
                    JobPrices = jobInfo.Prices,
                    PrieceDetailId = objectPriceDetail.Id,
                    Status = Enum.OrderStatus.IsWaiting,
                    CustomerAddress = request.Address,
                    CustomerAddressPoint = request.AddressPoint,
                    CustomerId = customerInfo.Id,
                    Note = request.Note,
                };
                var objectOrder = await _orderRepository.InsertAsync(order, true);
                if (objectOrder == null)
                {
                    await _prieceDetailRepository.DeleteAsync(objectPriceDetail.Id);
                    return new ApiErrorResult<OrderResponse>(message: "Có lỗi trong quá trình đặt đơn!");
                }
                var typeOfJob = await _typeOfJobRepository.GetAsync(jobInfo.TypeOfJobId);
                var orderResponse = new OrderResponse()
                {
                    Id = objectOrder.Id,
                    CreationTime = objectOrder.CreationTime.ToString("dd-MM-yyy HH:mm"),
                    CustomerId = customerIdentityInfo.Id,
                    WorkerId = wokerIdentityInfo.Id,
                    CustomerImage = String.IsNullOrEmpty(customerInfo.Avatar) ? Constants.ImageDefaultCustomer : customerInfo.Avatar,
                    WorkerImage = String.IsNullOrEmpty(workerInfo.Avatar) ? Constants.ImageDefaultWorker : workerInfo.Avatar,
                    CustomerName = customerIdentityInfo.Name,
                    WorkerName = wokerIdentityInfo.Name,
                    CustomerPhone = customerIdentityInfo.PhoneNumber,
                    WorkerPhone = wokerIdentityInfo.PhoneNumber,
                    IsPaid = objectOrder.IsPaid,
                    JobId = request.JobInfoId,
                    JobInfoImage = typeOfJob.Avatar,
                    JobInfoName = jobInfo.Name,
                    Note = objectOrder.Note,
                    Status = objectOrder.Status,
                    UserAddress = objectOrder.CustomerAddress,
                    UserPoint = objectOrder.CustomerAddressPoint,
                    JobPrices = jobInfo.Prices.ToString(),
                };
                return new ApiSuccessResult<OrderResponse>(resultObj: orderResponse);
            }
            catch (Exception)
            {
                return new ApiErrorResult<OrderResponse>(message: "Có lỗi trong quá trình đặt đơn!");
            }
        }
        [HttpGet]
        [Route("api/app/order/detail/{idOrder}")]
        public async Task<ApiResult<OrderResponse>> GetOrderDetailById(Guid idOrder)
        {
            try
            {
                var order = await _orderRepository.GetAsync(idOrder);
                var customerInfo = await _customerInfoRepository.GetAsync(order.CustomerId);
                var workerInfo = await _workerInfoRepository.GetAsync(order.WorkerId);
                var workerIdentityInfo = await _identityUserRepository.GetAsync(workerInfo.UserId);
                var customerIdentityInfo = await _identityUserRepository.GetAsync(customerInfo.UserId);
                var jobInfo = await _jobInfoRepository.GetAsync(order.JobInfoId);
                var typeOfJob = await _typeOfJobRepository.GetAsync(jobInfo.TypeOfJobId);
                var orderResponse = new OrderResponse()
                {
                    Id = order.Id,
                    CreationTime = order.CreationTime.ToString("dd-MM-yyyy HH-mm"),
                    CustomerId = customerIdentityInfo.Id,
                    CustomerImage = String.IsNullOrEmpty(customerInfo.Avatar) ? Constants.ImageDefaultCustomer : customerInfo.Avatar,
                    CustomerName = customerIdentityInfo.Name,
                    CustomerPhone = customerIdentityInfo.PhoneNumber,
                    WorkerName = workerIdentityInfo.Name,
                    WorkerPhone = workerIdentityInfo.PhoneNumber,
                    WorkerId = workerIdentityInfo.Id,
                    WorkerImage = String.IsNullOrEmpty(workerInfo.Avatar) ? Constants.ImageDefaultCustomer : workerInfo.Avatar,
                    JobInfoName = jobInfo.Name,
                    IsPaid = order.IsPaid,
                    JobId = jobInfo.Id,
                    JobInfoImage = typeOfJob.Avatar,
                    JobPrices = order.JobPrices.ToString(),
                    Note = order.Note,
                    Status = order.Status,
                    UserAddress = order.CustomerAddress,
                    UserPoint = order.CustomerAddressPoint,
                };
                return new ApiSuccessResult<OrderResponse>(resultObj: orderResponse);
            }
            catch (Exception)
            {
                return new ApiErrorResult<OrderResponse>(message: "Có lỗi trong quá trình lấy dữ liệu!");
            }
        }

        [HttpPut]
        [Route("api/app/order/{orderId}/update-status")]
        public async Task<ApiResult<OrderResponse>> UpdateStatusOrder(Guid orderId, OrderStatus status)
        {
            try
            {
                var order = await _orderRepository.GetAsync(orderId);
                if (order == null)
                {
                    return new ApiErrorResult<OrderResponse>(message: "Không tìm thấy đơn hàng!");
                }
                order.Status = status;
                var result = await _orderRepository.UpdateAsync(order);
                if (result == null)
                {
                    return new ApiErrorResult<OrderResponse>(message: "Cập nhật không thành công");
                }
                var customerInfo = await _customerInfoRepository.GetAsync(result.CustomerId);
                var workerInfo = await _workerInfoRepository.GetAsync(result.WorkerId);
                var workerIdentityInfo = await _identityUserRepository.GetAsync(workerInfo.UserId);
                var customerIdentityInfo = await _identityUserRepository.GetAsync(customerInfo.UserId);
                var jobInfo = await _jobInfoRepository.GetAsync(result.JobInfoId);
                var typeOfJob = await _typeOfJobRepository.GetAsync(jobInfo.TypeOfJobId);
                var orderResponse = new OrderResponse()
                {
                    Id = order.Id,
                    CreationTime = order.CreationTime.ToString("dd-MM-yyyy HH-mm"),
                    CustomerId = customerIdentityInfo.Id,
                    CustomerImage = String.IsNullOrEmpty(customerInfo.Avatar) ? Constants.ImageDefaultCustomer : customerInfo.Avatar,
                    CustomerName = customerIdentityInfo.Name,
                    CustomerPhone = customerIdentityInfo.PhoneNumber,
                    WorkerName = workerIdentityInfo.Name,
                    WorkerPhone = workerIdentityInfo.PhoneNumber,
                    WorkerId = workerIdentityInfo.Id,
                    WorkerImage = String.IsNullOrEmpty(workerInfo.Avatar) ? Constants.ImageDefaultCustomer : workerInfo.Avatar,
                    JobInfoName = jobInfo.Name,
                    IsPaid = order.IsPaid,
                    JobId = jobInfo.Id,
                    JobInfoImage = typeOfJob.Avatar,
                    JobPrices = order.JobPrices.ToString(),
                    Note = order.Note,
                    Status = order.Status,
                    UserAddress = order.CustomerAddress,
                    UserPoint = order.CustomerAddressPoint,
                };
                return new ApiSuccessResult<OrderResponse>(resultObj: orderResponse);
            }
            catch (Exception)
            {
                return new ApiErrorResult<OrderResponse>(message: "Có lỗi trong quá trình xử lý!");
            }
        }
        private static IEnumerable<DateTime> EachMonth(DateTime from, DateTime thru)
        {
            for (var month = from.Date; month.Date <= thru.Date || month.Month == thru.Month; month = month.AddMonths(1))
                yield return month;
        }
    }
}
