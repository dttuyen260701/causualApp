using Microsoft.EntityFrameworkCore;
using PBL6.CasualManager.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Volo.Abp.Domain.Repositories.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore;

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
    }
}
