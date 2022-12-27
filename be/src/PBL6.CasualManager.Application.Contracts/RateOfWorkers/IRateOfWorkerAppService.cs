using PBL6.CasualManager.ApiResults;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace PBL6.CasualManager.RateOfWorkers
{
    public interface IRateOfWorkerAppService
    {
        Task<ApiResult<RateOfWorkerResponse>> GetRateOfWorkerAsync(Guid workerId);
        Task<ApiResult<List<RateOfWorkerDetailResponse>>> GetListRateOfWorker(Guid workerId);
        Task<ApiResult<RateOfWorkerDetailResponse>> GetRateOfWorkerByOrderId(Guid orderId);
        Task<ApiResult<CreateRateOfWorkerResponse>> CreateRateOfWorker(Guid customerId, CreateRateOfWorkerRequest request);
    }
}
