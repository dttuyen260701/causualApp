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
        Task<PagedResultDto<OrderDto>> GetListByUserAsync(OrderConditionSearchDto condition);

        Task<Dictionary<string, string>> GetListRevenueOfMonthsOfWorkerAsync(Guid workerId, int month);
    }
    
}
