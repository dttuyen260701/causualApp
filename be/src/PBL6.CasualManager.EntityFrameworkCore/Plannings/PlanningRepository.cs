using Microsoft.EntityFrameworkCore;
using PBL6.CasualManager.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Dynamic.Core;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Domain.Repositories.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore;
using static Microsoft.EntityFrameworkCore.DbLoggerCategory;

namespace PBL6.CasualManager.Plannings
{
    public class PlanningRepository : EfCoreRepository<CasualManagerDbContext, Planning, Guid>, IPlanningRepository
    {
        public PlanningRepository(IDbContextProvider<CasualManagerDbContext> dbContextProvider) : base(dbContextProvider)
        {
        }

        public async Task<Dictionary<string, string>> GetListRevenueTargetOfMonths(int month = 1, int year = 0)
        {
            year = year == 0 ? DateTime.Now.Year : year;
            var dbSet = await GetDbSetAsync();
            var monthNow = DateTime.Now.Month;
            var query = dbSet
                .Where(x => x.Year == year)
                .Where(x => x.Month >= monthNow - month + 1).ToList();

            return query
                .GroupBy(x => x.Month)
                .Select(x =>
                {
                    return new
                    {
                        Month = x.Key,
                        Target = x.Sum(c => c.RevenueTarget)
                    };
                })
                .ToDictionary(x => x.Month.ToString(), x => x.Target.ToString());
        }

        public async Task<PagedResultDto<Planning>> GetListRevenueTargetByYearAsync(
            int skipCount,
            int maxResultCount,
            string sorting,
            int year)
        {
            PagedResultDto<Planning> list = new PagedResultDto<Planning>();
            var dbSet = await GetDbSetAsync();
            var query = dbSet
                .Where(x => x.Year == year);
            list.Items = await query
                .Skip(skipCount)
                .Take(maxResultCount)
                .OrderBy(sorting)
                .AsNoTracking()
                .ToListAsync();
            list.TotalCount = await query.CountAsync();
            return list;
        }

        public async Task<bool> IsExistTargetInYear(int year)
        {
            var dbSet = await GetDbSetAsync();
            return dbSet.Any(x => x.Year == year);
        }
    }
}
