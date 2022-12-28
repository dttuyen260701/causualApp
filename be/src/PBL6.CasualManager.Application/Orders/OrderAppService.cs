using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.Oders;
using PBL6.CasualManager.PagingModels;
using PBL6.CasualManager.Permissions;
using PBL6.CasualManager.PrieceDetails;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.WorkerInfos;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;
using Volo.Abp.Domain.Repositories;
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

        [Authorize(CasualManagerPermissions.Order.Default)]
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

        [Authorize(CasualManagerPermissions.Order.Default)]
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

        [Authorize(CasualManagerPermissions.Statisic.Default)]
        public async Task<Dictionary<string, string>> GetListRevenueOfMonthsOfWorkerAsync(Guid workerId, int month = 1)
        {
            var result = await _orderRepository.GetListRevenueOfMonthsOfWorker(workerId, month);
            return result;
        }

        [Authorize(CasualManagerPermissions.Statisic.Default)]
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

        [HttpGet]
        [Route("api/app/order/{workerId}/history-order-six-month")]
        public async Task<ApiResult<List<OrderOfWokerForChartResponse>>> GetListHistoryOrderOfWorkerSixMonth(Guid workerId)
        {
            var listOrderOfWokerForChartResponse = new List<OrderOfWokerForChartResponse>();
            var listOrder = await _orderRepository.GetListAsync(x => x.WorkerInfo.UserId == workerId, includeDetails: true);
            DateTime today = DateTime.Now;
            DateTime previousSixMonth = today.AddMonths(-5);
            var listPrieceDetail = await _prieceDetailRepository.GetListAsync();
            var result = (from order in listOrder
                          join prieceDetail in listPrieceDetail on order.PrieceDetailId equals prieceDetail.Id
                          where order.CreationTime.Month >= previousSixMonth.Month && order.CreationTime.Month <= today.Month && order.Status == OrderStatus.IsComplete
                          select new { prieceDetail, order } into x
                          group x by x.order.CreationTime.Month into g
                          select new OrderOfWokerForChartResponse()
                          {
                              Month = g.Key.ToString(),
                              Revenue = g.Select(x => x.prieceDetail.FeeForWorker).Sum().ToString(),
                              TotalOrder = g.Select(x => x.order).Count().ToString()
                          }).OrderBy(x => int.Parse(x.Month)).ToList();
            return new ApiSuccessResult<List<OrderOfWokerForChartResponse>>(resultObj: result);
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
                var customerIdentityInfo = await _identityUserRepository.GetAsync(idCustomer);
                var customerInfo = await _customerInfoRepository.GetEntityCustomerInfoHaveUserId(customerIdentityInfo.Id);
                //check if customer ordered worker and status is not cancel or done or reject, customer cannot order 
                var existOrder = await _orderRepository.FirstOrDefaultAsync(x => x.WorkerId == workerInfo.Id && x.CustomerId == customerInfo.Id
                                                                                        && (x.Status == OrderStatus.IsAccepted || x.Status == OrderStatus.IsInProcess || x.Status == OrderStatus.IsWaiting || x.Status == OrderStatus.IsTracking));
                if (existOrder != null)
                {
                    return new ApiErrorResult<OrderResponse>(message: "Bạn đang đặt đơn thợ này. Vui lòng tiếp tục thực hiện!");
                }
                var jobInfo = await _jobInfoRepository.GetAsync(request.JobInfoId);
                var priceDetail = new PrieceDetail()
                {
                    FeeForWorker = jobInfo.Prices - 5000,
                    FeeForBussiness = 5000
                };
                var objectPriceDetail = await _prieceDetailRepository.InsertAsync(priceDetail, true);
                if (objectPriceDetail == null)
                {
                    return new ApiErrorResult<OrderResponse>(message: "Có lỗi trong quá trình đặt đơn!");
                }
                var wokerIdentityInfo = await _identityUserRepository.GetAsync(request.WorkerId);
                var order = new Order()
                {
                    WorkerId = workerInfo.Id,
                    IsPaid = false,
                    JobInfoId = request.JobInfoId,
                    JobPrices = jobInfo.Prices,
                    PrieceDetailId = objectPriceDetail.Id,
                    Status = request.Status,
                    CustomerAddress = request.Address,
                    CustomerAddressPoint = request.AddressPoint,
                    CustomerId = customerInfo.Id,
                    Note = request.Note,
                    IsRead = false
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
                    IsRead = objectOrder.IsRead,
                };
                return new ApiSuccessResult<OrderResponse>(resultObj: orderResponse);
            }
            catch (Exception)
            {
                return new ApiErrorResult<OrderResponse>(message: "Có lỗi trong quá trình đặt đơn!");
            }
        }
        [HttpGet]
        [Route("api/app/order/detail/{idOrder}/by/{userId}")]
        public async Task<ApiResult<OrderResponse>> GetOrderDetailById(Guid idOrder, Guid userId)
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
                    CreationTime = order.CreationTime.ToString("dd-MM-yyyy HH:mm"),
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
                    IsRead = order.IsRead
                };
                if (await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(userId) != null)//check user is worker
                {
                    //update status isRead in order is true
                    if (!order.IsRead)
                    {
                        order.IsRead = true;
                        await _orderRepository.UpdateAsync(order);
                    }
                }
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
                if (status == OrderStatus.IsComplete)
                {
                    order.IsPaid = true;
                }
                if (status == OrderStatus.IsCancel)
                {
                    order.IsRead = true;
                }
                var result = await _orderRepository.UpdateAsync(order, true);
                if (result == null)
                {
                    return new ApiErrorResult<OrderResponse>(message: "Chuyển đổi trạng thái không thành công");
                }
                //update worker status after update order
                var workerInfo = await _workerInfoRepository.GetAsync(result.WorkerId);
                if (order.Status == OrderStatus.IsAccepted || order.Status == OrderStatus.IsInProcess || order.Status == OrderStatus.IsTracking)
                {
                    if (workerInfo.Status != WorkerStatus.Busy)
                    {
                        workerInfo.Status = WorkerStatus.Busy;
                        await _workerInfoRepository.UpdateAsync(workerInfo, true);
                    }
                }
                else if (order.Status == OrderStatus.IsComplete || order.Status == OrderStatus.IsCancel)
                {
                    if (workerInfo.Status != WorkerStatus.Free)
                    {
                        workerInfo.Status = WorkerStatus.Free;
                        await _workerInfoRepository.UpdateAsync(workerInfo, true);
                    }
                }


                var customerInfo = await _customerInfoRepository.GetAsync(result.CustomerId);
                var workerIdentityInfo = await _identityUserRepository.GetAsync(workerInfo.UserId);
                var customerIdentityInfo = await _identityUserRepository.GetAsync(customerInfo.UserId);
                var jobInfo = await _jobInfoRepository.GetAsync(result.JobInfoId);
                var typeOfJob = await _typeOfJobRepository.GetAsync(jobInfo.TypeOfJobId);
                var orderResponse = new OrderResponse()
                {
                    Id = order.Id,
                    CreationTime = order.CreationTime.ToString("dd-MM-yyyy HH:mm"),
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
                    IsRead = result.IsRead,
                };
                return new ApiSuccessResult<OrderResponse>(resultObj: orderResponse);
            }
            catch (Exception)
            {
                return new ApiErrorResult<OrderResponse>(message: "Có lỗi trong quá trình xử lý!");
            }
        }

        [HttpGet]
        [Route("api/app/order/{userId}/list-order-by-status")]
        public async Task<ApiResult<PagedResult<OrderResponse>>> GetOrdersByStatus(Guid userId, int status, PagingRequest paging)
        {
            try
            {
                var userIdentity = await _identityUserRepository.GetAsync(userId);
                bool isCustomer = true;
                var customerInfo = await _customerInfoRepository.GetEntityCustomerInfoHaveUserId(userIdentity.Id);
                WorkerInfo workerInfo = null;
                if (customerInfo == null)
                {
                    isCustomer = false;
                    workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(userIdentity.Id);
                }
                List<Order> orders = null;
                var listTypeOfJob = await _typeOfJobRepository.GetListAsync();
                var listJobInfo = await _jobInfoRepository.GetListAsync();
                if (isCustomer)
                {
                    switch (status)
                    {
                        case 0:
                            orders = await _orderRepository.GetListAsync(x => x.CustomerId == customerInfo.Id && x.Status == OrderStatus.IsWaiting);
                            break;
                        case 1:
                            orders = await _orderRepository.GetListAsync(x => x.CustomerId == customerInfo.Id && (x.Status == OrderStatus.IsAccepted || x.Status == OrderStatus.IsInProcess || x.Status == OrderStatus.IsTracking));
                            break;
                        case 2:
                            orders = await _orderRepository.GetListAsync(x => x.CustomerId == customerInfo.Id && (x.Status == OrderStatus.IsComplete
                            || x.Status == OrderStatus.IsCancel || x.Status == OrderStatus.IsRejected));
                            break;
                        default:
                            orders = await _orderRepository.GetListAsync(x => x.CustomerId == customerInfo.Id && x.Status == OrderStatus.IsWaiting);
                            break;
                    }
                    var workerInfos = await _workerInfoRepository.GetListAsync();
                    var listUserIdentity = await _identityUserRepository.GetListAsync();
                    var all = (from order in orders
                               join workerInf in workerInfos on order.WorkerId equals workerInf.Id
                               join userIden in listUserIdentity on workerInf.UserId equals userIden.Id
                               join jobInfo in listJobInfo on order.JobInfoId equals jobInfo.Id
                               join typeOfJob in listTypeOfJob on jobInfo.TypeOfJobId equals typeOfJob.Id
                               orderby order.CreationTime descending
                               select new OrderResponse()
                               {
                                   Id = order.Id,
                                   CreationTime = order.CreationTime.ToString("dd-MM-yyyy HH:mm"),
                                   CustomerId = customerInfo.Id,
                                   CustomerName = userIdentity.Name,
                                   CustomerImage = String.IsNullOrEmpty(customerInfo.Avatar) ? Constants.ImageDefaultCustomer : customerInfo.Avatar,
                                   CustomerPhone = userIdentity.PhoneNumber,
                                   WorkerId = workerInf.UserId,
                                   WorkerImage = String.IsNullOrEmpty(workerInf.Avatar) ? Constants.ImageDefaultWorker : workerInf.Avatar,
                                   WorkerName = userIden.Name,
                                   WorkerPhone = userIden.PhoneNumber,
                                   IsPaid = order.IsPaid,
                                   JobId = jobInfo.Id,
                                   JobInfoName = jobInfo.Name,
                                   JobInfoImage = typeOfJob.Avatar,
                                   Note = order.Note,
                                   JobPrices = order.JobPrices.ToString(),
                                   Status = order.Status,
                                   UserAddress = order.CustomerAddress,
                                   UserPoint = order.CustomerAddressPoint,
                                   IsRead = order.IsRead
                               }).ToList();
                    var listFilter = all.Skip((paging.PageIndex - 1) * paging.PageSize).Take(paging.PageSize).ToList();
                    return new ApiSuccessResult<PagedResult<OrderResponse>>(resultObj: new PagedResult<OrderResponse>()
                    {
                        Items = listFilter,
                        PageIndex = paging.PageIndex,
                        PageSize = paging.PageSize,
                        TotalRecords = all.Count()
                    });
                }
                else//if user is worker
                {
                    switch (status)
                    {
                        case 0:
                            orders = await _orderRepository.GetListAsync(x => x.WorkerId == workerInfo.Id && x.Status == OrderStatus.IsWaiting);
                            break;
                        case 1:
                            orders = await _orderRepository.GetListAsync(x => x.WorkerId == workerInfo.Id && (x.Status == OrderStatus.IsAccepted || x.Status == OrderStatus.IsInProcess || x.Status == OrderStatus.IsTracking));
                            break;
                        case 2:
                            orders = await _orderRepository.GetListAsync(x => x.WorkerId == workerInfo.Id && (x.Status == OrderStatus.IsComplete
                            || x.Status == OrderStatus.IsCancel || x.Status == OrderStatus.IsRejected));
                            break;
                        default:
                            orders = await _orderRepository.GetListAsync(x => x.WorkerId == workerInfo.Id && x.Status == OrderStatus.IsWaiting);
                            break;
                    }
                    var customerInfos = await _customerInfoRepository.GetListAsync();
                    var listUserIdentity = await _identityUserRepository.GetListAsync();
                    var all = (from order in orders
                               join customerInf in customerInfos on order.CustomerId equals customerInf.Id
                               join userIden in listUserIdentity on customerInf.UserId equals userIden.Id
                               join jobInfo in listJobInfo on order.JobInfoId equals jobInfo.Id
                               join typeOfJob in listTypeOfJob on jobInfo.TypeOfJobId equals typeOfJob.Id
                               orderby order.CreationTime descending
                               select new OrderResponse()
                               {
                                   Id = order.Id,
                                   CreationTime = order.CreationTime.ToString("dd-MM-yyyy HH:mm"),
                                   CustomerId = customerInf.Id,
                                   CustomerName = userIden.Name,
                                   CustomerImage = String.IsNullOrEmpty(customerInf.Avatar) ? Constants.ImageDefaultCustomer : customerInf.Avatar,
                                   CustomerPhone = userIden.PhoneNumber,
                                   WorkerId = workerInfo.UserId,
                                   WorkerImage = String.IsNullOrEmpty(workerInfo.Avatar) ? Constants.ImageDefaultWorker : workerInfo.Avatar,
                                   WorkerName = userIdentity.Name,
                                   WorkerPhone = userIdentity.PhoneNumber,
                                   IsPaid = order.IsPaid,
                                   JobId = jobInfo.Id,
                                   JobInfoName = jobInfo.Name,
                                   JobInfoImage = typeOfJob.Avatar,
                                   Note = order.Note,
                                   JobPrices = order.JobPrices.ToString(),
                                   Status = order.Status,
                                   UserAddress = order.CustomerAddress,
                                   UserPoint = order.CustomerAddressPoint,
                                   IsRead = order.IsRead
                               }).ToList();
                    var listFilter = all.Skip((paging.PageIndex - 1) * paging.PageSize).Take(paging.PageSize).ToList();
                    return new ApiSuccessResult<PagedResult<OrderResponse>>(resultObj: new PagedResult<OrderResponse>()
                    {
                        Items = listFilter,
                        PageIndex = paging.PageIndex,
                        PageSize = paging.PageSize,
                        TotalRecords = all.Count()
                    });
                }
            }
            catch (Exception)
            {
                return new ApiErrorResult<PagedResult<OrderResponse>>(message: "Đã xảy ra lỗi trong quá trình lấy dữ liệu!");
            }
        }

        [HttpGet]
        [Route("api/app/order/{workerId}/list-order")]
        public async Task<ApiResult<PagedResult<OrderResponse>>> GetOrdersOfWorker(Guid workerId, PagingRequest paging)
        {
            try
            {
                var userIdentity = await _identityUserRepository.GetAsync(workerId);
                var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(workerId);
                if (workerInfo == null)
                {
                    return new ApiErrorResult<PagedResult<OrderResponse>>(message: "Không tìm thấy thông tin người dùng!");
                }
                var orders = await _orderRepository.GetListAsync(x => x.WorkerId == workerInfo.Id);
                var customerInfos = await _customerInfoRepository.GetListAsync();
                var listUserIdentity = await _identityUserRepository.GetListAsync();
                var listTypeOfJob = await _typeOfJobRepository.GetListAsync();
                var listJobInfo = await _jobInfoRepository.GetListAsync();
                var all = (from order in orders
                           join customerInf in customerInfos on order.CustomerId equals customerInf.Id
                           join userIden in listUserIdentity on customerInf.UserId equals userIden.Id
                           join jobInfo in listJobInfo on order.JobInfoId equals jobInfo.Id
                           join typeOfJob in listTypeOfJob on jobInfo.TypeOfJobId equals typeOfJob.Id
                           where jobInfo.Name.ToLower().Contains(paging.Keyword ?? "")
                           orderby order.CreationTime descending
                           select new OrderResponse()
                           {
                               Id = order.Id,
                               CreationTime = order.CreationTime.ToString("dd-MM-yyyy HH:mm"),
                               CustomerId = customerInf.Id,
                               CustomerName = userIden.Name,
                               CustomerImage = String.IsNullOrEmpty(customerInf.Avatar) ? Constants.ImageDefaultCustomer : customerInf.Avatar,
                               CustomerPhone = userIden.PhoneNumber,
                               WorkerId = workerInfo.UserId,
                               WorkerImage = String.IsNullOrEmpty(workerInfo.Avatar) ? Constants.ImageDefaultWorker : workerInfo.Avatar,
                               WorkerName = userIdentity.Name,
                               WorkerPhone = userIdentity.PhoneNumber,
                               IsPaid = order.IsPaid,
                               JobId = jobInfo.Id,
                               JobInfoName = jobInfo.Name,
                               JobInfoImage = typeOfJob.Avatar,
                               Note = order.Note,
                               JobPrices = order.JobPrices.ToString(),
                               Status = order.Status,
                               UserAddress = order.CustomerAddress,
                               UserPoint = order.CustomerAddressPoint,
                               IsRead = order.IsRead
                           }).ToList();
                var result = all.Skip((paging.PageIndex - 1) * paging.PageSize).Take(paging.PageSize).ToList();
                return new ApiSuccessResult<PagedResult<OrderResponse>>(resultObj: new PagedResult<OrderResponse>()
                {
                    Items = result,
                    PageIndex = paging.PageIndex,
                    PageSize = paging.PageSize,
                    TotalRecords = all.Count()
                });
            }
            catch (Exception)
            {
                return new ApiErrorResult<PagedResult<OrderResponse>>(message: "Lỗi trong quá trình lấy dữ liệu!");
            }
        }

        private static IEnumerable<DateTime> EachMonth(DateTime from, DateTime thru)
        {
            for (var month = from.Date; month.Date <= thru.Date || month.Month == thru.Month; month = month.AddMonths(1))
                yield return month;
        }
    }
}
