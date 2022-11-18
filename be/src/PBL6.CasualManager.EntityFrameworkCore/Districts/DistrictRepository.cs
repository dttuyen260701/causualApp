using Microsoft.EntityFrameworkCore;
using PBL6.CasualManager.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Domain.Repositories.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore;

namespace PBL6.CasualManager.Districts
{
    public class DistrictRepository : EfCoreRepository<CasualManagerDbContext, District, Guid>, IDistrictRepository
    {
        public DistrictRepository(IDbContextProvider<CasualManagerDbContext> dbContextProvider) : base(dbContextProvider)
        {
        }

        public async Task<List<District>> GetDistrictsBelongProvinceAsync(Guid idProvince)
        {
            var result = await (from district in await GetDbSetAsync() where district.ProvinceId == idProvince select district).ToListAsync();
            return result;
        }
    }
}
