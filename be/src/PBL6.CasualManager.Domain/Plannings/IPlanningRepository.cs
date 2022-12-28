using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Domain.Repositories;

namespace PBL6.CasualManager.Plannings
{
    public interface IPlanningRepository : IRepository<Planning, Guid>
    {
        Task<Dictionary<string, string>> GetListRevenueTargetOfMonths(int month = 1, int year = 0);

        Task<PagedResultDto<Planning>> GetListRevenueTargetByYearAsync(
            int skipCount,
            int maxResultCount,
            string sorting,
            int year);

        Task<bool> IsExistTargetInYear(int year);
    }
}
