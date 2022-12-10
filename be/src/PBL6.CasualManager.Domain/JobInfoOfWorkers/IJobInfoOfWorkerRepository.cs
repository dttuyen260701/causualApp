using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Domain.Repositories;

namespace PBL6.CasualManager.JobInfoOfWorkers
{
    public interface IJobInfoOfWorkerRepository : IRepository<JobInfoOfWorker, Guid>
    {
        Task<PagedResultDto<JobInfoOfWorker>> SearchList(
            int skipCount,
            int maxResultCount,
            string sorting,
            Guid? filterWorker,
            string filterJobName,
            Guid? filterTypeOfJob);

        Task<JobInfoOfWorker> GetByJobInfoAndWorker(Guid jobInfoId, Guid workerId);
    }
}
