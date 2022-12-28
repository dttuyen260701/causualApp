using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.Orders;
using PBL6.CasualManager.PagingModels;
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

        Task<ApiResult<OrderResponse>> GetOrderDetailById(Guid idOrder, Guid userId);

        Task<ApiResult<OrderResponse>> UpdateStatusOrder(Guid orderId, OrderStatus status);

        Task<ApiResult<PagedResult<OrderResponse>>> GetOrdersByStatus(Guid userId, int status, PagingRequest paging);

        Task<ApiResult<PagedResult<OrderResponse>>> GetOrdersOfWorker(Guid workerId, PagingRequest paging);
    }
    
}
