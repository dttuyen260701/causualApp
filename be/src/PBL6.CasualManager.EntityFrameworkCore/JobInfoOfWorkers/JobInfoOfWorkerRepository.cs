using PBL6.CasualManager.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Domain.Repositories.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore;

namespace PBL6.CasualManager.JobInfoOfWorkers
{
    public class JobInfoOfWorkerRepository : EfCoreRepository<CasualManagerDbContext, JobInfoOfWorker, Guid>, IJobInfoOfWorkerRepository
    {
        public JobInfoOfWorkerRepository(IDbContextProvider<CasualManagerDbContext> dbContextProvider) : base(dbContextProvider)
        {
        }
    }
}
