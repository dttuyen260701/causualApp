using System;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;

namespace PBL6.CasualManager.WorkerInfos
{
    public interface IWorkerInfoAppService :
        ICrudAppService<
            WorkerInfoDto,
            Guid,
            WorkerInfoConditionSearchDto,
            WorkerInfoCreateUpdateDto>
    {
        Task<PagedResultDto<WorkerInfoDto>> GetListWorkerAllInfoAsync(WorkerInfoConditionSearchDto condition);

        Task<WorkerInfoDto> GetWorkerInfoAsync(Guid id);

        Task CreateWorkerInfoAsync(WorkerInfoCreateUpdateDto workerInfoCreateUpdateDto);

        Task UpdateWorkerInfoAsync(Guid id, WorkerInfoCreateUpdateDto workerInfoCreateUpdateDto);

        Task DeleteWorkerInfoAsync(Guid id);
    }
}
