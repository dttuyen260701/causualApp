using PBL6.CasualManager.Oders;
using System;
using System.Collections.Generic;
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

        public OrderAppService(
            IOrderRepository orderRepository,
            IIdentityUserRepository identityUserRepository) : base(orderRepository)
        {
            _orderRepository = orderRepository;
            _identityUserRepository = identityUserRepository;
        }

        public async Task<PagedResultDto<OrderDto>> GetListByUserAsync(OrderConditionSearchDto condition)
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
    }
}
