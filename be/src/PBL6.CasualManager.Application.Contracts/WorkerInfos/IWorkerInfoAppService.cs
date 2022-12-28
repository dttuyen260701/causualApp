using PBL6.CasualManager.Accounts;
using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.PagingModels;
using System;
using System.Collections.Generic;
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

        Task<ApiResult<List<WorkerInfoResponse>>> GetListWorkerVm(Guid idCustomer);

        Task<ApiResult<PagedResult<WorkerInfoResponse>>> GetListWorkerByJobInfo(Guid idUser, Guid idJobInfo, PagingRequest paging);

        Task<ApiResult<WorkerInfoResponse>> GetWorkerInfoDetail(Guid id);

        Task<ApiResult<WorkerInfoRateRevenueTotalJobResponse>> Get_TotalJob_TotalRevenue_TotalOrder(Guid workerId);

        Task<ApiResult<List<JobInfoResponse>>> GetListJobInfoBelongToWoker(Guid id);
    }
}
