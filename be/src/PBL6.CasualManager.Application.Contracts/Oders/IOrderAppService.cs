using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.Orders;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;

namespace PBL6.CasualManager.Oders
{
    public interface IOrderAppService :
        ICrudAppService<
            OrderDto,
            Guid,
            OrderConditionSearchDto,
            OrderCreateUpdateDto>
    {
        Task<PagedResultDto<OrderDto>> GetListByWorkerAsync(OrderConditionSearchDto condition);

        Task<PagedResultDto<OrderDto>> GetListByCustomerAsync(OrderConditionSearchDto condition);

        Task<Dictionary<string, string>> GetListRevenueOfMonthsOfWorkerAsync(Guid workerId, int month);

        Task<ApiResult<List<OrderOfCustomerResponse>>> GetListHistoryOrderOfCustomer(Guid idCustomer);

        Task<ApiResult<List<OrderOfWokerForChartResponse>>> GetListHistoryOrderOfWorkerSixMonth(Guid idWorker);

        Task<ApiResult<OrderResponse>> CreateOrder(Guid idCustomer, CreateOrderRequest request);

        Task<ApiResult<OrderResponse>> GetOrderDetailById(Guid idOrder);

        Task<ApiResult<OrderResponse>> UpdateStatusOrder(Guid orderId, OrderStatus status);
    }
    
}
