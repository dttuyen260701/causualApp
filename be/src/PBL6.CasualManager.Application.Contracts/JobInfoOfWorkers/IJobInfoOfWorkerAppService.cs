using System;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;

namespace PBL6.CasualManager.JobInfoOfWorkers
{
    public interface IJobInfoOfWorkerAppService :
        ICrudAppService<
            JobInfoOfWorkerDto,
            Guid,
            JobInfoOfWorkerConditionSearchDto,
            JobInfoOfWorkerCreateUpdateDto>
    {
        Task<PagedResultDto<JobInfoOfWorkerDto>> GetListSearchAsync(JobInfoOfWorkerConditionSearchDto condition);

        Task CreateJobInfoOfWorker(JobInfoOfWorkerCreateUpdateDto jobInfoOfWorkerCreateUpdateDto);

        Task UpdateJobInfoOfWorker(Guid id, JobInfoOfWorkerCreateUpdateDto jobInfoOfWorkerCreateUpdateDto);
    }
}
