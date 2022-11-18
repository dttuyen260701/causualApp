using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PBL6.CasualManager.Accounts;
using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.Enum;
using System;
using System.Globalization;
using System.IO;
using System.Threading.Tasks;
using Volo.Abp.Identity;

namespace PBL6.CasualManager.CustomerInfos
{
    public class CustomerInfoAppService : CasualManagerAppService, ICustomerInfoAppService
    {
        private readonly ICustomerInfoRepository _customerInfoRepository;
        private readonly IdentityUserManager _identityUserManager;
        private readonly IHostingEnvironment _hostingEnvironment;
        private readonly IIdentityUserRepository _identityUserRepository;
        public CustomerInfoAppService(IIdentityUserRepository identityUserRepository, IHostingEnvironment hostingEnvironment, ICustomerInfoRepository customerInfoRepository, IdentityUserManager identityUserManager)
        {
            _customerInfoRepository = customerInfoRepository;
            _identityUserManager = identityUserManager;
            _hostingEnvironment = hostingEnvironment;
            _identityUserRepository = identityUserRepository;
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

        public async Task<ApiResult<CustomerInfoAllDto>> UpdateAsync(Guid id, [FromForm] CustomerInfoUpdateRequest request)
         {
            if (id != request.Id)
            {
                return new ApiErrorResult<CustomerInfoAllDto>("Có lỗi trong quá trình xử lý");
            }
            IdentityUser identityUser = null;
            try
            {
                identityUser = await _identityUserRepository.GetAsync(id);
            }
            catch (Exception)
            {
                return new ApiErrorResult<CustomerInfoAllDto>("Không tìm thấy đối tượng. Vui lòng kiểm tra lại.");
            }
            if (identityUser == null)
            {
                return new ApiErrorResult<CustomerInfoAllDto>("Không tìm thấy đối tượng. Vui lòng kiểm tra lại.");
            }

            var customerInfo = await _customerInfoRepository.GetEntityCustomerInfoHaveUserId(request.Id);
            if (customerInfo != null)
            {
                try
                {
                    identityUser.Name = request.Name;
                    identityUser.SetPhoneNumber(request.Phone, true);
                    await _identityUserManager.UpdateAsync(identityUser);
                    customerInfo.Gender = request.Gender;
                    customerInfo.Address = request.Address;
                    customerInfo.AddressPoint = string.Empty;
                    customerInfo.DistrictId = request.DistrictId;
                    customerInfo.DistrictName = request.DistrictName;
                    customerInfo.WardId = request.WardId;
                    customerInfo.WardName = request.WardName;
                    customerInfo.ProvinceId = request.ProvinceId;
                    customerInfo.ProvinceName = request.ProvinceName;
                    customerInfo.DateOfBirth = DateTime.ParseExact(request.DateOfBirth, "dd-MM-yyyy", CultureInfo.GetCultureInfo("tr-TR"));
                    customerInfo.Avatar = await SaveImageAsync(request.Avatar, customerInfo.UserId) ?? customerInfo.Avatar;

                    await _customerInfoRepository.UpdateAsync(customerInfo, true);
                    var identityUserAfterUpdate = await _identityUserManager.GetByIdAsync(id);
                    var infoCustomerAfterUpdate = await _customerInfoRepository.GetAsync(customerInfo.Id);

                    var customerInfoAllDto = new CustomerInfoAllDto()
                    {
                        Email = identityUserAfterUpdate.Email,
                        Id = identityUserAfterUpdate.Id,
                        UserName = identityUserAfterUpdate.UserName,
                        Name = identityUserAfterUpdate.Name,
                        Phone = identityUserAfterUpdate.PhoneNumber,
                        Gender = infoCustomerAfterUpdate.Gender,
                        Address = infoCustomerAfterUpdate.Address,
                        AddressPoint = infoCustomerAfterUpdate.AddressPoint,
                        DateOfBirth = infoCustomerAfterUpdate.DateOfBirth.ToString("dd-MM-yyyy"),
                        ProvinceId = infoCustomerAfterUpdate.ProvinceId,
                        ProvinceName = infoCustomerAfterUpdate.ProvinceName,
                        WardId = infoCustomerAfterUpdate.WardId,
                        WardName = infoCustomerAfterUpdate.WardName,
                        DistrictId = infoCustomerAfterUpdate.DistrictId,
                        DistrictName = infoCustomerAfterUpdate.DistrictName,
                        Avatar = infoCustomerAfterUpdate.Avatar,
                        Role = Role.CUSTOMER
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
        private async Task<string> SaveImageAsync(IFormFile image, Guid idUser)
        {
            if (image == null)
            {
                return null;
            }
            try
            {
                string nameFile = Constants.PrefixAvatarCustomer + idUser.ToString();
                string newPathFile = Constants.LinkToFolderImageCustomer + nameFile + ".png";
                using (FileStream fileStream = System.IO.File.Create(_hostingEnvironment.WebRootPath + "/" + newPathFile))
                {
                    await image.CopyToAsync(fileStream);
                    await fileStream.FlushAsync();
                }
                return newPathFile;
            }
            catch (Exception)
            {
                return null;
            }
        }
    }
}