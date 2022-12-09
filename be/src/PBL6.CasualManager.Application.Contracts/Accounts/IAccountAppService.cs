using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.WorkerInfos;
using System;
using System.Threading.Tasks;

namespace PBL6.CasualManager.Accounts
{
    public interface IAccountAppService
    {
        Task<ApiResult<string>> RegisterAsCustomer(RegisterCustomerRequest request);

        Task<ApiResult<string>> RegisterAsWorker(RegisterWorkerRequest request);

        Task<ApiResult<CustomerInfoAllResponse>> EditCustomerInfoAsync(Guid id, CustomerInfoUpdateRequest request);

        Task<ApiResult<WorkerInfoAllResponse>> EditWorkerInfoAsync(Guid id, WorkerInfoUpdateRequest request);

        Task<ApiResult<UserInfoAllDto>> PostLogin(LoginRequest request);

        Task<ApiResult<string>> ChangePasswordAsync(Guid userId, ChangePasswordRequest request);
    }

}
