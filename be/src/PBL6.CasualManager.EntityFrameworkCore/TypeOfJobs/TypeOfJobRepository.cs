using Microsoft.EntityFrameworkCore;
using PBL6.CasualManager.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Domain.Repositories.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore;

namespace PBL6.CasualManager.TypeOfJobs
{
    public class TypeOfJobRepository :
        EfCoreRepository<CasualManagerDbContext, TypeOfJob, Guid>,
        ITypeOfJobRepository
    {

        public TypeOfJobRepository(IDbContextProvider<CasualManagerDbContext> dbContextProvider) : base(dbContextProvider)
        {
        }

        public async Task<PagedResultDto<TypeOfJob>> GetListByName(
            int skipCount,
            int maxResultCount,
            string sorting,
            string filterName = null)
        {
            PagedResultDto<TypeOfJob> list = new PagedResultDto<TypeOfJob>();
            var dbSet = await GetDbSetAsync();
            list.Items = await dbSet
                .WhereIf(
                    !filterName.IsNullOrWhiteSpace(),
                    x => x.Name.ToLower().Contains(filterName.ToLower())
                )
                .Skip(skipCount)
                .Take(maxResultCount)
                .AsNoTracking()
                .ToListAsync();
            list.TotalCount = list.Items.Count;
            return list;
        }
    }
}
