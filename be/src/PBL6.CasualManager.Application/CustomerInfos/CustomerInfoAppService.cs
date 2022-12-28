using Microsoft.AspNetCore.Authorization;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.FileStorages;
using PBL6.CasualManager.LookupValues;
using PBL6.CasualManager.Permissions;
using PBL6.CasualManager.TypeOfJobs;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;
using Volo.Abp.Identity;

namespace PBL6.CasualManager.CustomerInfos
{
    public class CustomerInfoAppService : 
        CrudAppService<
            CustomerInfo,
            CustomerInfoDto,
            Guid,
            CustomerInfoConditionSearchDto,
            CustomerInfoCreateUpdateDto>,
        ICustomerInfoAppService
    {
        private readonly IFileStorageAppService _fileStorageAppService;
        private readonly ICustomerInfoRepository _customerInfoRepository;
        private readonly IdentityUserManager _identityUserManager;
        private readonly IIdentityUserRepository _identityUserRepository;
        public CustomerInfoAppService(
            IFileStorageAppService fileStorageAppService,
            IIdentityUserRepository identityUserRepository,
            ICustomerInfoRepository customerInfoRepository,
            IdentityUserManager identityUserManager) : base(customerInfoRepository)
        {
            _fileStorageAppService = fileStorageAppService;
            _customerInfoRepository = customerInfoRepository;
            _identityUserManager = identityUserManager;
            _identityUserRepository = identityUserRepository;
        }

        [Authorize(CasualManagerPermissions.CustomerInfo.Default)]
        public async Task<PagedResultDto<CustomerInfoDto>> GetListCustomerAllInfoAsync(CustomerInfoConditionSearchDto condition)
        {
            var listCustomerInfoDto = await _customerInfoRepository.GetListAsync();
            var listCustomerInfoAllDto = new List<CustomerInfoDto>();
            foreach (var customer in listCustomerInfoDto)
            {
                var customerIdentity = await _identityUserRepository.GetAsync(customer.UserId);
                var customerInfoAllDto = ObjectMapper.Map<CustomerInfo, CustomerInfoDto>(customer);
                customerInfoAllDto.Name = customerIdentity.Name;
                customerInfoAllDto.Email = customerIdentity.Email;
                customerInfoAllDto.Phone = customerIdentity.PhoneNumber;
                customerInfoAllDto.UserName = customerIdentity.UserName;
                listCustomerInfoAllDto.Add(customerInfoAllDto);
            }
            listCustomerInfoAllDto = listCustomerInfoAllDto
                .WhereIf(!condition.Keyword.IsNullOrWhiteSpace(),
                    x => x.Name.ToLower().Contains(condition.Keyword.ToLower())).ToList();
            return new PagedResultDto<CustomerInfoDto> { Items = listCustomerInfoAllDto, TotalCount = listCustomerInfoAllDto.Count };
        }

        [Authorize(CasualManagerPermissions.CustomerInfo.Default)]
        public async Task<CustomerInfoDto> GetCustomerInfoAsync(Guid id)
        {
            var customerInfo = await _customerInfoRepository.GetAsync(id);
            var customerIdentity = await _identityUserRepository.GetAsync(customerInfo.UserId);
            var result = ObjectMapper.Map<CustomerInfo, CustomerInfoDto>(customerInfo);
            result.Name = customerIdentity.Name;
            result.Email = customerIdentity.Email;
            result.Phone = customerIdentity.PhoneNumber;
            result.UserName = customerIdentity.UserName;
            return result;
        }

        [Authorize(CasualManagerPermissions.CustomerInfo.Create)]
        public async Task CreateCustomerInfoAsync(CustomerInfoCreateUpdateDto customerInfoCreateUpdateDto)
        {
            if (await _identityUserManager.FindByEmailAsync(customerInfoCreateUpdateDto.Email) != null)
            {
                throw new EmailExistException(customerInfoCreateUpdateDto.Email);
            }
            if (await _identityUserManager.FindByNameAsync(customerInfoCreateUpdateDto.UserName) != null)
            {
                throw new UserNameExistException(customerInfoCreateUpdateDto.UserName);
            }
            if (await _identityUserRepository.GetCountAsync(phoneNumber: customerInfoCreateUpdateDto.Phone) > 0)
            {
                throw new PhoneExistException(customerInfoCreateUpdateDto.Phone);
            }

            var identityUserCreate = new IdentityUser(Guid.NewGuid(), customerInfoCreateUpdateDto.UserName, customerInfoCreateUpdateDto.Email);
            identityUserCreate.SetPhoneNumber(customerInfoCreateUpdateDto.Phone, true);
            identityUserCreate.Name = customerInfoCreateUpdateDto.Name;

            try
            {
                var result = await _identityUserManager.CreateAsync(identityUserCreate, customerInfoCreateUpdateDto.Password);
                if (!result.Succeeded)
                {
                    throw new InfoWrongException();
                }
                var resultAddRole = await _identityUserManager.AddToRoleAsync(identityUserCreate, Role.CUSTOMER);
                if (!resultAddRole.Succeeded)
                {
                    await _identityUserManager.DeleteAsync(identityUserCreate);
                    throw new SetUpRoleException();
                }
            }
            catch (Exception ex)
            {
                await _identityUserManager.DeleteAsync(identityUserCreate);
                throw ex;
            }

            var customerInfoCreate = ObjectMapper.Map<CustomerInfoCreateUpdateDto, CustomerInfo>(customerInfoCreateUpdateDto);
            customerInfoCreate.UserId = identityUserCreate.Id;
            customerInfoCreate.AddressPoint = "";
            await _customerInfoRepository.InsertAsync(customerInfoCreate);
        }

        [Authorize(CasualManagerPermissions.CustomerInfo.Update)]
        public async Task UpdateCustomerInfoAsync(Guid id, CustomerInfoCreateUpdateDto customerInfoCreateUpdateDto)
        {
            var customerInfo = await _customerInfoRepository.GetAsync(id);
            var customerIdentity = await _identityUserRepository.GetAsync(customerInfo.UserId).ConfigureAwait(false);
            if ((await _identityUserRepository.GetCountAsync(phoneNumber: customerInfoCreateUpdateDto.Phone) > 0) && customerInfoCreateUpdateDto.Phone != customerIdentity.PhoneNumber)
            {
                throw new PhoneExistException(customerInfoCreateUpdateDto.Phone);
            }
            customerInfo.Gender = customerInfoCreateUpdateDto.Gender;
            customerInfo.Address = customerInfoCreateUpdateDto.Address;
            customerInfo.AddressPoint = string.Empty;
            customerInfo.DistrictId = customerInfoCreateUpdateDto.DistrictId;
            customerInfo.DistrictName = customerInfoCreateUpdateDto.DistrictName;
            customerInfo.WardId = customerInfoCreateUpdateDto.WardId;
            customerInfo.WardName = customerInfoCreateUpdateDto.WardName;
            customerInfo.ProvinceId = customerInfoCreateUpdateDto.ProvinceId;
            customerInfo.ProvinceName = customerInfoCreateUpdateDto.ProvinceName;
            customerInfo.DateOfBirth = customerInfoCreateUpdateDto.DateOfBirth;
            customerInfo.Avatar = customerInfoCreateUpdateDto.Avatar;
            await _customerInfoRepository.UpdateAsync(customerInfo);

            customerIdentity.Name = customerInfoCreateUpdateDto.Name;
            customerIdentity.SetPhoneNumber(customerInfoCreateUpdateDto.Phone, true);
            await _identityUserManager.UpdateAsync(customerIdentity);
        }

        [Authorize(CasualManagerPermissions.CustomerInfo.Delete)]
        public async Task DeleteCustomerInfoAsync(Guid id)
        {
            var customerInfo = await _customerInfoRepository.GetAsync(id);
            var customerIdentity = await _identityUserRepository.GetAsync(customerInfo.UserId).ConfigureAwait(false);
            var deleteIdentity = await _identityUserManager.DeleteAsync(customerIdentity);
            if (deleteIdentity.Succeeded)
            {
                await _customerInfoRepository.DeleteAsync(customerInfo);
            }
            if (customerInfo.Avatar != null)
            {
                _fileStorageAppService.DeleteImageAsync(customerInfo.Avatar);
            }
        }

        public async Task<List<LookupValueDto>> GetLookupValuesAsync()
        {
            var listCustomerInfo = await _customerInfoRepository.GetListAsync();
            var listLookupValueDtos = new List<LookupValueDto>();
            foreach (var customer in listCustomerInfo)
            {
                var customerIdentity = await _identityUserRepository.GetAsync(customer.UserId);
                var lookupValueDto = ObjectMapper.Map<CustomerInfo, LookupValueDto>(customer);
                lookupValueDto.Name = customerIdentity.Name;
                listLookupValueDtos.Add(lookupValueDto);
            }
            return listLookupValueDtos;
        }

    }
}