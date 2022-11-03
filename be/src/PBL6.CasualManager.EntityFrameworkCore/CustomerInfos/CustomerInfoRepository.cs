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
    }
}
