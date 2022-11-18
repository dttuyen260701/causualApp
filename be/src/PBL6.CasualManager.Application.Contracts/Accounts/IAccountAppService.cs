using PBL6.CasualManager.ApiResults;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace PBL6.CasualManager.Accounts
{
    public interface IAccountAppService
    {
        Task<ApiResult<string>> RegisterAsCustomer(RegisterCustomerRequest request);

        Task<ApiResult<string>> RegisterAsWorker(RegisterWorkerRequest request);

        Task<ApiResult<UserInfoAllDto>> PostLogin(LoginRequest request);
    }

}
