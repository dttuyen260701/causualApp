using Microsoft.AspNetCore.Http;
using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.WorkerInfos;
using System;
using System.IO;
using System.Threading.Tasks;
using Volo.Abp.Identity;
using Microsoft.AspNetCore.Hosting;
using PBL6.CasualManager.Enum;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Identity;
using System.Globalization;

namespace PBL6.CasualManager.Accounts
{
    public class AccountAppService : CasualManagerAppService, IAccountAppService
    {
        private readonly IdentityUserManager _identityUserManager;
        private readonly IWorkerInfoRepository _workerInfoRepository;
        private readonly ICustomerInfoRepository _customerInfoRepository;
        private readonly IIdentityUserRepository _identityUserRepository;
        private readonly IHostingEnvironment _hostingEnvironment;
        private readonly SignInManager<IdentityUser> _signInManager;
        public AccountAppService(SignInManager<IdentityUser> signInManager, IWorkerInfoRepository workerInfoRepository, IIdentityUserRepository identityUserRepository,
            ICustomerInfoRepository customerInfoRepository, IdentityUserManager identityUserManager, IHostingEnvironment hostingEnvironment)
        {
            _customerInfoRepository = customerInfoRepository;
            _workerInfoRepository = workerInfoRepository;
            _identityUserManager = identityUserManager;
            _identityUserRepository = identityUserRepository;
            _hostingEnvironment = hostingEnvironment;
            _signInManager = signInManager;
        }

        public async Task<ApiResult<UserInfoAllDto>> PostLogin(LoginRequest request)
        {
            if (request == null)
            {
                return new ApiErrorResult<UserInfoAllDto>(message: "Không hợp lệ");
            }
            IdentityUser userIdentity = null;
            try
            {
                userIdentity = await _identityUserManager.FindByEmailAsync(request.UserName)
                ?? await _identityUserManager.FindByNameAsync(request.UserName);
            }
            catch (Exception)
            {
                return new ApiErrorResult<UserInfoAllDto>(message: "Có lỗi trong quá trình đăng nhập!");
            }
            
            if(userIdentity == null)
            {
                return new ApiErrorResult<UserInfoAllDto>("Tài khoản không tồn tại");
            }
            var signInResult = await _signInManager.CheckPasswordSignInAsync(
                userIdentity,
                request.Password,
                false
            );
            if(signInResult.Succeeded)
            {
                var roles = await _identityUserManager.GetRolesAsync(userIdentity);  
                var userInfoAllDto = new UserInfoAllDto()
                {
                    Id = userIdentity.Id,
                    Phone = userIdentity.PhoneNumber,
                    Email = userIdentity.Email,
                    Name = userIdentity.Name,
                    UserName = userIdentity.UserName,
                };
                if (roles.Contains(Role.CUSTOMER))
                {
                    if(request.WithRole == WithRole.WhateverRole || request.WithRole == WithRole.Customer)
                    {
                        var customerInfo = await _customerInfoRepository.GetEntityCustomerInfoHaveUserId(userIdentity.Id);
                        userInfoAllDto.DistrictId = customerInfo.DistrictId;
                        userInfoAllDto.DistrictName = customerInfo.DistrictName;
                        userInfoAllDto.WardId = customerInfo.WardId;
                        userInfoAllDto.WardName = customerInfo.WardName;
                        userInfoAllDto.ProvinceId = customerInfo.ProvinceId;
                        userInfoAllDto.ProvinceName = customerInfo.ProvinceName;
                        userInfoAllDto.DateOfBirth = customerInfo.DateOfBirth.ToString("dd-MM-yyyy");
                        userInfoAllDto.Gender = customerInfo.Gender;
                        userInfoAllDto.Avatar = customerInfo.Avatar;
                        userInfoAllDto.Address = customerInfo.Address;
                        userInfoAllDto.AddressPoint = customerInfo.AddressPoint;
                        userInfoAllDto.Role = Role.CUSTOMER;
                        userInfoAllDto.LastModificationTime = customerInfo.LastModificationTime;
                    }
                    else
                    {
                        return new ApiErrorResult<UserInfoAllDto>(message: "Vai trò của bạn không được đăng nhập ở đây!");
                    }
                }
                if(roles.Contains(Role.WORKER))
                {
                    if (request.WithRole == WithRole.WhateverRole || request.WithRole == WithRole.Worker)
                    {
                        var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(userIdentity.Id);
                        userInfoAllDto.DistrictId = workerInfo.DistrictId;
                        userInfoAllDto.DistrictName = workerInfo.DistrictName;
                        userInfoAllDto.WardId = workerInfo.WardId;
                        userInfoAllDto.WardName = workerInfo.WardName;
                        userInfoAllDto.ProvinceId = workerInfo.ProvinceId;
                        userInfoAllDto.ProvinceName = workerInfo.ProvinceName;
                        userInfoAllDto.DateOfBirth = workerInfo.DateOfBirth.ToString("dd-MM-yyyy");
                        userInfoAllDto.Gender = workerInfo.Gender;
                        userInfoAllDto.Avatar = workerInfo.Avatar;
                        userInfoAllDto.Address = workerInfo.Address;
                        userInfoAllDto.AddressPoint = workerInfo.AddressPoint;
                        userInfoAllDto.IdentityCard = workerInfo.IdentityCard;
                        userInfoAllDto.IdentityCardBy = workerInfo.IdentityCardBy;
                        userInfoAllDto.IdentityCardDate = workerInfo.IdentityCardDate.ToShortDateString();
                        userInfoAllDto.StartWorkingTime = workerInfo.StartWorkingTime;
                        userInfoAllDto.EndWorkingTime = workerInfo.EndWorkingTime;
                        userInfoAllDto.LastModificationTime = workerInfo.LastModificationTime;
                        userInfoAllDto.Role = Role.WORKER;
                    }
                    else
                    {
                        return new ApiErrorResult<UserInfoAllDto>(message: "Vai trò của bạn không được đăng nhập ở đây!");
                    }

                }
                return new ApiSuccessResult<UserInfoAllDto>(userInfoAllDto);
            }
            return new ApiErrorResult<UserInfoAllDto>("Mật khẩu hoặc tài khoản đăng nhập không đúng");
        }

        public async Task<ApiResult<string>> RegisterAsCustomer(RegisterCustomerRequest request)
        {
            if (await _identityUserManager.FindByEmailAsync(request.Email) != null)
            {
                return new ApiErrorResult<string>(message: $"Email {request.Email} đã được đăng ký!");
            }
            if (await _identityUserManager.FindByNameAsync(request.UserName) != null)
            {
                return new ApiErrorResult<string>(message: $"UserName {request.UserName} đã được đăng ký!");
            }
            if (await _identityUserRepository.GetCountAsync(phoneNumber: request.Phone) > 0)
            {
                return new ApiErrorResult<string>(message: $"Số điện thoại {request.Phone} đã được đăng ký!");
            }

            //create info identity user
            var identityUser = new IdentityUser(Guid.NewGuid(), request.UserName, request.Email);
            identityUser.SetPhoneNumber(request.Phone, true);
            identityUser.Name = request.Name;
            try
            {
                var result = await _identityUserManager.CreateAsync(identityUser, request.Password);
                if(!result.Succeeded)
                {
                    return new ApiErrorResult<string>("Vui lòng kiểm tra lại thông tin");
                }
                var resultAddRole = await _identityUserManager.AddToRoleAsync(identityUser, Role.CUSTOMER);
                if (!resultAddRole.Succeeded)
                {
                    await _identityUserManager.DeleteAsync(identityUser);
                    return new ApiErrorResult<string>("Tạo không thành công");
                } 
            }
            catch (Exception)
            {
                await _identityUserManager.DeleteAsync(identityUser);
                return new ApiErrorResult<string>("Có lỗi trong quá trình xử lý");
            }

            //create info customer
            try
            {
                var customerInfo = new CustomerInfo()
                {
                    UserId = identityUser.Id,
                    Gender = Gender.Undefined,
                    Address = String.Empty,
                    AddressPoint = String.Empty,
                    Avatar = String.Empty,  
                    DateOfBirth = DateTime.Now,
                    ProvinceId = String.Empty,
                    ProvinceName = String.Empty,
                    WardId = String.Empty,
                    WardName = String.Empty,
                    DistrictId = String.Empty,
                    DistrictName = String.Empty,
                };
                await _customerInfoRepository.InsertAsync(customerInfo, true);
                return new ApiSuccessResult<string>();
            }
            catch (Exception)
            {
                await _identityUserManager.DeleteAsync(identityUser);
                return new ApiErrorResult<string>(message: "Có lỗi trong quá trình xử lý!");
            }
        }

        public async Task<ApiResult<string>> RegisterAsWorker(RegisterWorkerRequest request)
        {
            if (await _identityUserManager.FindByEmailAsync(request.Email) != null)
            {
                return new ApiErrorResult<string>(message: $"Email {request.Email} đã được đăng ký!");
            }
            if (await _identityUserManager.FindByNameAsync(request.UserName) != null)
            {
                return new ApiErrorResult<string>(message: $"UserName {request.UserName} đã được đăng ký!");
            }
            if (await _identityUserRepository.GetCountAsync(phoneNumber: request.Phone) > 0)
            {
                return new ApiErrorResult<string>(message: $"Số điện thoại {request.Phone} đã được đăng ký!");
            }
            if (await _workerInfoRepository.CheckExistIdentityCard(request.IdentityCard))
            {
                return new ApiErrorResult<string>("Số chứng minh thư đã được đăng ký. Vui lòng kiểm tra lại");
            }
            //create info identity user
            var identityUser = new IdentityUser(Guid.NewGuid(), request.UserName, request.Email);
            identityUser.SetPhoneNumber(request.Phone, true);
            identityUser.Name = request.Name;
            try
            {
                var result = await _identityUserManager.CreateAsync(identityUser, request.Password);
                if (!result.Succeeded)
                {
                    return new ApiErrorResult<string>("Đăng ký không thành công. Vui lòng kiểm tra lại thông tin");
                }
                var resultAddRole = await _identityUserManager.AddToRoleAsync(identityUser, Role.WORKER);
                if (!resultAddRole.Succeeded)
                {
                    await _identityUserManager.DeleteAsync(identityUser);
                    return new ApiErrorResult<string>("Đăng ký không thành công");
                }
            }
            catch (Exception)
            {
                await _identityUserRepository.DeleteAsync(identityUser);
                return new ApiErrorResult<string>("Đăng ký không thành công");
            }

            //create info worker
            try
            {
                var workerInfo = new WorkerInfo()
                {
                    UserId = identityUser.Id,
                    Address = request.Address,
                    AddressPoint = String.Empty,
                    Gender = request.Gender,
                    AverageRate = 0,
                    DateOfBirth = DateTime.ParseExact(request.DateOfBirth, "dd-MM-yyyy", CultureInfo.GetCultureInfo("tr-TR")),
                    IdentityCard = request.IdentityCard,
                    StartWorkingTime = request.StartWorkingTime,
                    EndWorkingTime = request.EndWorkingTime,
                    DistrictId = request.DistrictId,
                    DistrictName = request.DistrictName,
                    IdentityCardBy = request.IdentityCardBy,
                    IdentityCardDate = DateTime.ParseExact(request.IdentityCardDate, "dd-MM-yyyy", CultureInfo.GetCultureInfo("tr-TR")),
                    IsActive = true,
                    ProvinceId = request.ProvinceId,
                    ProvinceName = request.ProvinceName,
                    WardId = request.WardId,
                    WardName = request.WardName,
                    Avatar = string.Empty,
                    Status = WorkerStatus.Free,
                };
                var result = await _workerInfoRepository.InsertAsync(workerInfo, true);
                return new ApiSuccessResult<string>();
            }
            catch (Exception)
            {
                await _identityUserRepository.DeleteAsync(identityUser);
                return new ApiErrorResult<string>("Đăng ký không thành công");
            }
        }

        [HttpPut]
        [Route("/api/app/account/edit-customer-info/{id}")]
        public async Task<ApiResult<CustomerInfoAllResponse>> EditCustomerInfoAsync(Guid id, [FromForm] CustomerInfoUpdateRequest request)
        {
            if (id != request.Id)
            {
                return new ApiErrorResult<CustomerInfoAllResponse>("Có lỗi trong quá trình xử lý");
            }
            IdentityUser identityUser = null;
            try
            {
                identityUser = await _identityUserRepository.GetAsync(id).ConfigureAwait(false);
            }
            catch (Exception)
            {
                return new ApiErrorResult<CustomerInfoAllResponse>("Không tìm thấy đối tượng. Vui lòng kiểm tra lại.");
            }
            if (identityUser == null)
            {
                return new ApiErrorResult<CustomerInfoAllResponse>("Không tìm thấy đối tượng. Vui lòng kiểm tra lại.");
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

                    var customerInfoAllDto = new CustomerInfoAllResponse()
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
                    return new ApiSuccessResult<CustomerInfoAllResponse>(customerInfoAllDto);
                }
                catch (Exception)
                {
                    return new ApiErrorResult<CustomerInfoAllResponse>("Có lỗi trong quá trình xử lý");
                }
            }
            return new ApiErrorResult<CustomerInfoAllResponse>("Không tìm thấy đối tượng. Vui lòng kiểm tra lại.");
        }

        [HttpPut]
        [Route("/api/app/account/edit-worker-info/{id}")]
        public async Task<ApiResult<WorkerInfoAllResponse>> EditWorkerInfoAsync(Guid id, [FromForm] WorkerInfoUpdateRequest request)
        {
            if (id != request.Id)
            {
                return new ApiErrorResult<WorkerInfoAllResponse>("Có lỗi trong quá trình xử lý");
            }
            var identityUser = await _identityUserManager.GetByIdAsync(id);
            if (identityUser == null)
            {
                return new ApiErrorResult<WorkerInfoAllResponse>("Không tìm thấy đối tượng");
            }
            var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(request.Id);
            if (workerInfo != null)
            {
                try
                {
                    identityUser.Name = request.Name;
                    identityUser.SetPhoneNumber(request.Phone, true);
                    await _identityUserManager.UpdateAsync(identityUser);
                    workerInfo.Gender = request.Gender;
                    workerInfo.Address = request.Address;
                    workerInfo.AddressPoint = string.Empty;
                    workerInfo.DistrictId = request.DistrictId;
                    workerInfo.DistrictName = request.DistrictName;
                    workerInfo.WardId = request.WardId;
                    workerInfo.WardName = request.WardName;
                    workerInfo.ProvinceId = request.ProvinceId;
                    workerInfo.ProvinceName = request.ProvinceName;
                    workerInfo.DateOfBirth = DateTime.ParseExact(request.DateOfBirth, "dd-MM-yyyy", CultureInfo.GetCultureInfo("tr-TR"));
                    workerInfo.Avatar = await SaveImageAsync(request.Avatar, workerInfo.UserId) ?? workerInfo.Avatar;
                    workerInfo.IdentityCard = request.IdentityCard;
                    workerInfo.IdentityCardBy = request.IdentityCardBy;
                    workerInfo.IdentityCardDate = DateTime.ParseExact(request.IdentityCardDate, "dd-MM-yyyy", CultureInfo.GetCultureInfo("tr-TR"));
                    workerInfo.StartWorkingTime = request.StartWorkingTime;
                    workerInfo.EndWorkingTime = request.EndWorkingTime;

                    await _workerInfoRepository.UpdateAsync(workerInfo);

                    var identityUserAfterUpdate = await _identityUserManager.GetByIdAsync(id);
                    var infoWorkerAfterUpdate = await _workerInfoRepository.GetAsync(workerInfo.Id);

                    var workerInfoAllDto = new WorkerInfoAllResponse()
                    {
                        Email = identityUserAfterUpdate.Email,
                        Id = identityUserAfterUpdate.Id,
                        Username = identityUserAfterUpdate.UserName,
                        Name = identityUserAfterUpdate.Name,
                        Phone = identityUserAfterUpdate.PhoneNumber,
                        Gender = infoWorkerAfterUpdate.Gender,
                        Address = infoWorkerAfterUpdate.Address,
                        AddressPoint = infoWorkerAfterUpdate.AddressPoint,
                        IdentityCard = infoWorkerAfterUpdate.IdentityCard,
                        DateOfBirth = infoWorkerAfterUpdate.DateOfBirth.ToString("dd-MM-yyyy"),
                        ProvinceId = infoWorkerAfterUpdate.ProvinceId,
                        ProvinceName = infoWorkerAfterUpdate.ProvinceName,
                        WardId = infoWorkerAfterUpdate.WardId,
                        WardName = infoWorkerAfterUpdate.WardName,
                        DistrictId = infoWorkerAfterUpdate.DistrictId,
                        DistrictName = infoWorkerAfterUpdate.DistrictName,
                        Avatar = infoWorkerAfterUpdate.Avatar,
                        IdentityCardDate = infoWorkerAfterUpdate.IdentityCardDate.ToString("dd-MM-yyyy"),
                        IdentityCardBy = infoWorkerAfterUpdate.IdentityCardBy,
                        Role = Role.WORKER,
                    };
                    return new ApiSuccessResult<WorkerInfoAllResponse>(resultObj: workerInfoAllDto);
                }
                catch (Exception)
                {

                    return new ApiErrorResult<WorkerInfoAllResponse>("Có lỗi trong quá trình xử lý");
                }
            }
            return new ApiErrorResult<WorkerInfoAllResponse>("Không tìm thấy đối tượng");
        }

        private async Task<string> SaveImageAsync(IFormFile image, Guid idUser)
        {
            if(image == null)
            {
                return null;
            }
            try
            {
                string nameFile = Constants.PrefixAvatarWorker + idUser.ToString();
                string newPathFile = Constants.LinkToFolderImageWorker + nameFile + ".png";
                using (FileStream fileStream = System.IO.File.Create(_hostingEnvironment.WebRootPath + "/" + newPathFile))
                {
                    await image.CopyToAsync(fileStream);
                    await fileStream.FlushAsync();
                }
                return newPathFile;
            }
            catch(Exception)
            { 
                return null;
            }
        }
    }
}