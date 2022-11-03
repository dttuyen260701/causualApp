using PBL6.CasualManager.Accounts;
using PBL6.CasualManager.ApiResults;
using System;
using System.Threading.Tasks;
using Volo.Abp.Identity;

namespace PBL6.CasualManager.CustomerInfos
{
    public class CustomerInfoAppService : CasualManagerAppService, ICustomerInfoAppService
    {
        private readonly ICustomerInfoRepository _customerInfoRepository;
        private readonly IdentityUserManager _identityUserManager;

        public CustomerInfoAppService(ICustomerInfoRepository customerInfoRepository, IdentityUserManager identityUserManager)
        {
            _customerInfoRepository = customerInfoRepository;
            _identityUserManager = identityUserManager;
        }

        public async Task<bool> AddAsync(CustomerInfoDto customerInfosDto)
        {
            var customerInfo = ObjectMapper.Map<CustomerInfoDto, CustomerInfo>(customerInfosDto);
            try
            {
                await _customerInfoRepository.InsertAsync(customerInfo, true);
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public async Task<ApiResult<CustomerInfoAllDto>> UpdateAsync(Guid id, CustomerInfoUpdateRequest request)
        {
            if (id != request.UserId)
            {
                return new ApiErrorResult<CustomerInfoAllDto>("Có lỗi xử lý");
            }
            var identityUser = await _identityUserManager.GetByIdAsync(id);
            if (identityUser == null)
            {
                return new ApiErrorResult<CustomerInfoAllDto>("Không tìm thấy đối tượng. Vui lòng kiểm tra lại.");
            }

            var customerInfo = await _customerInfoRepository.GetEntityCustomerInfoHaveUserId(request.UserId);
            if (customerInfo != null)
            {
                try
                {
                    identityUser.Name = request.Name;
                    identityUser.SetPhoneNumber(request.Phone, true);
                    await _identityUserManager.UpdateAsync(identityUser);
                    customerInfo.Gender = request.Gender;
                    customerInfo.Address = request.Address;
                    customerInfo.AddressPoint = request.AddressPoint;

                    await _customerInfoRepository.UpdateAsync(customerInfo, true);
                    var identityUserAfterUpdate = await _identityUserManager.GetByIdAsync(id);
                    var infoCustomerAfterUpdate = await _customerInfoRepository.GetAsync(customerInfo.Id);

                    var customerInfoAllDto = new CustomerInfoAllDto()
                    {
                        Email = identityUserAfterUpdate.Email,
                        UserId = identityUserAfterUpdate.Id,
                        UserName = identityUserAfterUpdate.UserName,
                        Name = identityUserAfterUpdate.Name,
                        Phone = identityUserAfterUpdate.PhoneNumber,
                        Gender = infoCustomerAfterUpdate.Gender,
                        Address = infoCustomerAfterUpdate.Address,
                        AddressPoint = infoCustomerAfterUpdate.AddressPoint
                    };
                    return new ApiSuccessResult<CustomerInfoAllDto>(customerInfoAllDto);
                }
                catch (Exception)
                {
                    return new ApiErrorResult<CustomerInfoAllDto>("Có lỗi trong quá trình xử lý");
                }
            }
            return new ApiErrorResult<CustomerInfoAllDto>("Không tìm thấy đối tượng. Vui lòng kiểm tra lại.");
        }
    }
}