using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.WorkerInfos;
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
    }
}
