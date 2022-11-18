using Microsoft.EntityFrameworkCore;
using PBL6.CasualManager.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Domain.Repositories.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore;

namespace PBL6.CasualManager.Wards
{
    public class WardRepository : EfCoreRepository<CasualManagerDbContext, Ward, Guid>, IWardRepository
    {
        public WardRepository(IDbContextProvider<CasualManagerDbContext> dbContextProvider) : base(dbContextProvider)
        {
        }

        public async Task<List<Ward>> GetWardsBelongDistrict(Guid idDistrict)
        {
            var result = await (from ward in await GetDbSetAsync() where ward.DistrictId == idDistrict select ward).ToListAsync();
            return result;
        }
    }
}
