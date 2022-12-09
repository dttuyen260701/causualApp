using PBL6.CasualManager.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Domain.Repositories.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore;

namespace PBL6.CasualManager.RateOfWorkers
{
    public class RateOfWorkerRepository : EfCoreRepository<CasualManagerDbContext, RateOfWorker, Guid>, IRateOfWorkerRepository
    {
        public RateOfWorkerRepository(IDbContextProvider<CasualManagerDbContext> dbContextProvider) : base(dbContextProvider)
        {
        }
    }
}
