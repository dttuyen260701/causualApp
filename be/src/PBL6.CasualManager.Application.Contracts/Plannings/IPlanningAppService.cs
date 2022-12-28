using PBL6.CasualManager.Oders;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;

namespace PBL6.CasualManager.Plannings
{
    public interface IPlanningAppService :
        ICrudAppService<
            PlanningDto,
            Guid,
            PlanningConditionSearchDto,
            PlanningCreateUpdateDto>
    {
        Task<Dictionary<string, string>> GetListRevenueTargetOfMonthsAsync(int month = 1, int year = 0);

        Task<PagedResultDto<PlanningDto>> GetListRevenueTargetByYearAsync(PlanningConditionSearchDto condition);

        Task<List<PlanningDto>> GetListTargetByYearAsync(int year);

        Task<List<int>> GetListYearHaveData();

        Task CreateUpdateRevenueTargetInYear(PlanningCreateUpdateDto createUpdateData);
    }
}
