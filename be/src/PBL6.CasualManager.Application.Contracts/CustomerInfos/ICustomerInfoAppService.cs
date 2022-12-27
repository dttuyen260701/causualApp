using System;
using System.Threading.Tasks;
using PBL6.CasualManager.Accounts;
using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.LookupValues;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;

namespace PBL6.CasualManager.CustomerInfos
{
    public interface ICustomerInfoAppService :
        ICrudAppService<
            CustomerInfoDto,
            Guid,
            CustomerInfoConditionSearchDto,
            CustomerInfoCreateUpdateDto>,
        ILookupValueService
    {
        Task<PagedResultDto<CustomerInfoDto>> GetListCustomerAllInfoAsync(CustomerInfoConditionSearchDto condition);

        Task CreateCustomerInfoAsync(CustomerInfoCreateUpdateDto customerInfoCreateUpdateDto);

        Task UpdateCustomerInfoAsync(Guid id, CustomerInfoCreateUpdateDto customerInfoCreateUpdateDto);

        Task<CustomerInfoDto> GetCustomerInfoAsync(Guid id);
    }
}