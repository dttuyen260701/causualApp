using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.PagingModels;
using PBL6.CasualManager.WorkerInfos;
using System.Collections.Generic;
using System;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.PostOfDemands
{
    public interface IPostOfDemandAppService
    {
        Task<PagedResultDto<PostOfDemandDto>> GetListSearchAsync(PostOfDemandConditionSearchDto condition);
        Task<ApiResult<PagedResult<PostOfDemandResponse>>> GetListPostOfDemandForWorkerAsync(Guid workerId, PagingRequest paging);
        Task<ApiResult<PostOfDemandResponse>> CreatePostOfDemandAsync(Guid customerId, CreatePostOfDemandRequest request);
        Task<ApiResult<WorkerInfoResponse>> CreateWorkerResponsePostOfDemand(WorkerResponseRequest request);
        Task<ApiResult<List<WorkerRequestInPostOfDemandResponse>>> GetListWorkerInPostOfDemand(Guid postOfDemandId);
        Task<ApiResult<PagedResult<PostOfDemandResponse>>> GetListPostOfDemandForCustomer(Guid customerId, PagingRequest paging);//id is customer identity id
        Task<ApiResult<List<PostOfDemandResponse>>> GetListPostOfDemandWorkerRequest(Guid workerId);//id is worker identity id
        Task<ApiResult<PostOfDemandResponse>> GetPostOfDemandById(Guid id);
        Task<ApiResult<string>> DeleteWorkerReponsePostOfDemand(Guid workerId, Guid postOfDemandId);
        Task<ApiResult<string>> UnactivePostOfDemand(Guid id);
    }
}
