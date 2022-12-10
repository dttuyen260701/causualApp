using Microsoft.EntityFrameworkCore;
using PBL6.CasualManager.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Dynamic.Core;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Domain.Repositories.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore;

namespace PBL6.CasualManager.JobInfoOfWorkers
{
    public class JobInfoOfWorkerRepository :
        EfCoreRepository<CasualManagerDbContext, JobInfoOfWorker, Guid>, 
        IJobInfoOfWorkerRepository
    {
        public JobInfoOfWorkerRepository(IDbContextProvider<CasualManagerDbContext> dbContextProvider) : base(dbContextProvider)
        {
        }

        public async Task<PagedResultDto<JobInfoOfWorker>> SearchList(
            int skipCount, 
            int maxResultCount,
            string sorting,
            Guid? filterWorker = null,
            string filterJobName = null,
            Guid? filterTypeOfJob = null)
        {
            PagedResultDto<JobInfoOfWorker> list = new PagedResultDto<JobInfoOfWorker>();
            var dbSet = await GetDbSetAsync();
            var query = dbSet
                .Include(x => x.JobInfo).ThenInclude(y => y.TypeOfJob)
                .WhereIf(
                    filterTypeOfJob != null && filterTypeOfJob != Guid.Empty,
                    x => x.JobInfo.TypeOfJobId.Equals(filterTypeOfJob)
                )
                .WhereIf(
                    filterWorker != null && filterWorker != Guid.Empty,
                    x => x.WorkerId.Equals(filterWorker)
                )
                .WhereIf(
                    !filterJobName.IsNullOrWhiteSpace(),
                    x => x.JobInfo.Name.ToLower().Contains(filterJobName.ToLower()));
            list.Items = await query
                .OrderBy(x => x.JobInfo.TypeOfJobId)
                .Skip(skipCount)
                .Take(maxResultCount)
                .AsNoTracking()
                .ToListAsync();
            list.TotalCount = await query.CountAsync();
            return list;
        }

        public async Task<JobInfoOfWorker> GetByJobInfoAndWorker(Guid jobInfoId, Guid workerId)
        {
            var dbSet = await GetDbSetAsync();
            return dbSet
                .IncludeDetails(true)
                .FirstOrDefault(x => x.JobInfoId.Equals(jobInfoId) && x.WorkerId.Equals(workerId));
        }

        public override async Task<IQueryable<JobInfoOfWorker>> WithDetailsAsync()
        {
            return (await GetQueryableAsync()).IncludeDetails();
        }
    }
}
