using Microsoft.EntityFrameworkCore;
using PBL6.CasualManager.EntityFrameworkCore;
using PBL6.CasualManager.TypeOfJobs;
using System;
using System.Linq;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Domain.Repositories.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore;

namespace PBL6.CasualManager.JobInfos
{
    public class JobInfoRepository :
        EfCoreRepository<CasualManagerDbContext, JobInfo, Guid>,
        IJobInfoRepository
    {
        public JobInfoRepository(IDbContextProvider<CasualManagerDbContext> dbContextProvider) : base(dbContextProvider)
        {
        }

        public async Task<PagedResultDto<JobInfo>> GetListSearchAsync(
            int skipCount,
            int maxResultCount,
            string sorting,
            string filterName = null,
            Guid? filterTypeOfJob = null)
        {
            PagedResultDto<JobInfo> list = new PagedResultDto<JobInfo>();
            var dbSet = await GetDbSetAsync();
            list.Items = await dbSet
                .WhereIf(
                    filterTypeOfJob != null && filterTypeOfJob != Guid.Empty,
                    x => x.TypeOfJobId.Equals(filterTypeOfJob)
                )
                .IncludeDetails(true)
                .WhereIf(
                    !filterName.IsNullOrWhiteSpace(),
                    x => x.Name.ToLower().Contains(filterName.ToLower()) || x.TypeOfJob.Name.ToLower().Contains(filterName.ToLower())
                )
                .Skip(skipCount)
                .Take(maxResultCount)
                .AsNoTracking()
                .ToListAsync();
            list.TotalCount = list.Items.Count;
            return list;
        }

        public override async Task<IQueryable<JobInfo>> WithDetailsAsync()
        {
            return (await GetQueryableAsync()).IncludeDetails();
        }
    }
}
