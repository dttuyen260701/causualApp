using Microsoft.EntityFrameworkCore;
using PBL6.CasualManager.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Domain.Repositories.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore;

namespace PBL6.CasualManager.WorkerResponses
{
    public class WorkerResponseRepository : EfCoreRepository<CasualManagerDbContext, WorkerResponse, Guid>, IWorkerResponseRepository
    {
        public WorkerResponseRepository(IDbContextProvider<CasualManagerDbContext> dbContextProvider) : base(dbContextProvider)
        {
        }
    }
}
