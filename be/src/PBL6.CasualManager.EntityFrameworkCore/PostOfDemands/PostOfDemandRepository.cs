using Microsoft.EntityFrameworkCore;
using PBL6.CasualManager.EntityFrameworkCore;
using PBL6.CasualManager.Orders;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Domain.Repositories.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore;

namespace PBL6.CasualManager.PostOfDemands
{
    public class PostOfDemandRepository : EfCoreRepository<CasualManagerDbContext, PostOfDemand, Guid>, IPostOfDemandRepository
    {
        public PostOfDemandRepository(IDbContextProvider<CasualManagerDbContext> dbContextProvider) : base(dbContextProvider)
        {

        }

        public async Task<PagedResultDto<PostOfDemand>> GetListSearchAsync(
            int skipCount,
            int maxResultCount,
            string sorting,
            Guid? customerId)
        {
            PagedResultDto<PostOfDemand> list = new PagedResultDto<PostOfDemand>();
            var dbSet = await GetDbSetAsync();
            var query = dbSet
                .WhereIf(
                    customerId != null && customerId != Guid.Empty,
                    x => x.CustomerId.Equals(customerId)
                );
            list.Items = await query
                .Include(x => x.CustomerInfo)
                .Include(x => x.WorkerResponses)
                .Include(x => x.JobInfo)
                .Skip(skipCount)
                .Take(maxResultCount)
                .OrderByDescending(x => x.CreationTime)
                .AsNoTracking()
                .ToListAsync();
            list.TotalCount = await query.CountAsync();
            return list;
        }
    }
}
