using PBL6.CasualManager.ApiResults;
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

        Task<ApiResult<string>> RemoveJobInfoOfWorker(Guid jobInfoId, Guid workerId);

        Task<ApiResult<string>> RegisterJobForWorker(Guid idUser, RegisterJobRequest request);
    }
}
