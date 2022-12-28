using Microsoft.EntityFrameworkCore;
using PBL6.CasualManager.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Domain.Repositories;
using Volo.Abp.Domain.Repositories.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore;

namespace PBL6.CasualManager.CustomerInfos
{
    public class CustomerInfoRepository : EfCoreRepository<CasualManagerDbContext, CustomerInfo, Guid>, ICustomerInfoRepository
    {
        public CustomerInfoRepository(IDbContextProvider<CasualManagerDbContext> dbContextProvider) : base(dbContextProvider)
        {
        }
        public async Task<CustomerInfo> GetEntityCustomerInfoHaveUserId(Guid userId)
        {
            return await FindAsync(x => x.UserId == userId);
        }

        public async Task<List<CustomerInfo>> GetListTopCustomer(int take)
        {
            var dbSet = await GetDbSetAsync();
            var result = await dbSet
                .Include(x => x.Orders)
                .OrderByDescending(x => x.Orders.Count)
                .Take(take)
                .AsNoTracking()
                .ToListAsync();
            return result;
        }

        public async Task GetListCountOfCustomerInEachProvince()
        {
            var dbSet = await GetDbSetAsync();
            var result = dbSet
                .ToList()
                .GroupBy(x => x.ProvinceId)
                .Select(x =>
                {
                    return new
                    {
                        ProvinceId = x.Key,
                        ProvinceName = x.First().ProvinceName,
                        Count = x.Count()
                    };
                })
                .ToDictionary(x => x.ProvinceId, x => new Dictionary<string, int>() { { x.ProvinceName, x.Count} });
        }
    }
}
