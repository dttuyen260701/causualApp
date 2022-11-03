using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Domain.Repositories.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore;

namespace PBL6.CasualManager.WorkerInfos
{
    public class WorkerInfoRepository : EfCoreRepository<CasualManagerDbContext, WorkerInfo, Guid>, IWorkerInfoRepository
    {
        public WorkerInfoRepository(IDbContextProvider<CasualManagerDbContext> dbContextProvider) : base(dbContextProvider)
        {
        }
    }
}
