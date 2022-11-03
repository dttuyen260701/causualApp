using System;
using System.Threading.Tasks;
using PBL6.CasualManager.Accounts;
using PBL6.CasualManager.ApiResults;

namespace PBL6.CasualManager.CustomerInfos
{
    public interface ICustomerInfoAppService
    {
        Task<bool> AddAsync(CustomerInfoDto customerInfosDto);
        Task<ApiResult<CustomerInfoAllDto>> UpdateAsync(Guid id, CustomerInfoUpdateRequest request);
    }
}