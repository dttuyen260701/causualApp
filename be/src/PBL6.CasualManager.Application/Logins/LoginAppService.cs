using Microsoft.AspNetCore.Identity;
using PBL6.CasualManager.Accounts;
using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.WorkerInfos;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Identity;

namespace PBL6.CasualManager.Logins
{
    public class LoginAppService : CasualManagerAppService, ILoginAppService
    {
        private readonly IdentityUserManager _identityUserManager;
        private readonly SignInManager<IdentityUser> _signInManager;
        private readonly IWorkerInfoRepository _workerInfoRepository;
        private readonly ICustomerInfoRepository _customerInfoRepository;
        public LoginAppService(ICustomerInfoRepository customerInfoRepository, IWorkerInfoRepository workerInfoRepository, IdentityUserManager identityUserManager, SignInManager<IdentityUser> signInManager)
        {
            _identityUserManager = identityUserManager;
            _signInManager = signInManager;
            _workerInfoRepository = workerInfoRepository;
            _customerInfoRepository = customerInfoRepository;
        }

        public async Task<ApiResult<UserInfoAllDto>> PostLogin(LoginRequest request)
        {
            if(request == null)
            {
                return new ApiErrorResult<UserInfoAllDto>("Không hợp lệ");
            }
            var user = await _identityUserManager.FindByEmailAsync(request.Email);
            if (user == null)
            {
                return new ApiErrorResult<UserInfoAllDto>("Tài khoản không tồn tại");
            }    
            var signInResult = await _signInManager.PasswordSignInAsync(
                request.Email,
                request.Password,
                request.Rememberme,
                false
            );
            if(signInResult.Succeeded)
            {
                UserInfoAllDto userInfoAllDto = null;
                var roles = await _identityUserManager.GetRolesAsync(user);
                if(roles.Contains("customer"))
                {
                    var customerInfo = await _customerInfoRepository.GetEntityCustomerInfoHaveUserId(user.Id);
                    userInfoAllDto = new UserInfoAllDto()
                    {
                        Id = user.Id,
                        PhoneNumber = user.PhoneNumber,
                        Email = user.Email,
                        Name = user.Name,
                        Gender = customerInfo.Gender,
                        Address = customerInfo.Address,
                        AddressPoint = customerInfo.AddressPoint,
                        Role = "Customer"
                    };
                }    
                else
                {
                    var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(user.Id);
                    userInfoAllDto = new UserInfoAllDto()
                    {
                        Id = user.Id,
                        PhoneNumber = user.PhoneNumber,
                        Email = user.Email,
                        Name = user.Name,
                        Gender = workerInfo.Gender,
                        Address = workerInfo.Address,
                        AddressPoint = workerInfo.AddressPoint,
                        DateOfBirth = workerInfo.DateOfBirth,
                        IdentityCard = workerInfo.IdentityCard,
                        Role = "Worker",
                        Workingime = workerInfo.Workingime
                    };
                }    
                return new ApiSuccessResult<UserInfoAllDto>(userInfoAllDto);
            }
            return new ApiErrorResult<UserInfoAllDto>("Mật khẩu hoặc tài khoản đăng nhập không đúng");
        }
    }
}
