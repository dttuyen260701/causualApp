using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.WorkerInfos;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Threading.Tasks;
using Volo.Abp.Identity;
using Volo.Abp.Validation;

namespace PBL6.CasualManager.Accounts
{
    public class AccountAppService : CasualManagerAppService, IAccountAppService
    {
        private readonly IdentityUserManager _identityUserManager;
        private readonly ICustomerInfoAppService _customerInfoAppService;
        private readonly IWorkerInfoAppService _workerInfoAppService;
        private readonly IIdentityUserRepository _identityUserRepository;

        public AccountAppService(IWorkerInfoAppService workerInfoAppService, IIdentityUserRepository identityUserRepository,
            ICustomerInfoAppService customerInfoAppService, IdentityUserManager identityUserManager)
        {
            _workerInfoAppService = workerInfoAppService;
            _customerInfoAppService = customerInfoAppService;
            _identityUserManager = identityUserManager;
            _identityUserRepository = identityUserRepository;
        }

        public async Task<ApiResult<string>> RegisterAsCustomer(RegisterCustomerRequest request)
        {
            bool checkError = false;
            List<string> arrErr = new List<string>();
            if (await _identityUserManager.FindByEmailAsync(request.Email) != null)
            {
                checkError = true;
                arrErr.Add(request.Email + " đã tồn tại!");
            }
            if (await _identityUserRepository.GetCountAsync(phoneNumber: request.Phone) > 0)
            {
                checkError = true;
                arrErr.Add(request.Phone + " đã tồn tại!");
            }
            if (checkError == true)
            {
                return new ApiErrorResult<string>(arrErr);
            }

            //create info identity user
            var identityUser = new IdentityUser(Guid.NewGuid(), request.UserName, request.Email);
            identityUser.SetPhoneNumber(request.Phone, true);
            identityUser.Name = request.Name;
            try
            {
                await _identityUserManager.CreateAsync(identityUser);
                await _identityUserManager.AddPasswordAsync(identityUser, request.Password);
                await _identityUserManager.AddToRoleAsync(identityUser, "customer");
            }
            catch (Exception)
            {
                return new ApiErrorResult<string>("Có lỗi trong quá trình xử lý");
            }

            //create info customer
            try
            {
                var customerInfoDto = new CustomerInfoDto()
                {
                    UserId = identityUser.Id,
                    Address = request.Address,
                    AddressPoint = request.Address,
                    Gender = request.Gender
                };
                await _customerInfoAppService.AddAsync(customerInfoDto);
                return new ApiSuccessResult<string>();
            }
            catch (Exception)
            {
                return new ApiErrorResult<string>("Có lỗi trong quá trình xử lý");
            }
        }

        public async Task<ApiResult<string>> RegisterAsWorker(RegisterWorkerRequest request)
        {
            var existUser = await _identityUserManager.FindByEmailAsync(request.Email);
            if (existUser == null)
            {
                //create info identity user
                var identityUser = new IdentityUser(Guid.NewGuid(), request.UserName, request.Email);
                identityUser.SetPhoneNumber(request.Phone, true);
                identityUser.Name = request.Name;
                try
                {
                    await _identityUserManager.CreateAsync(identityUser);
                    await _identityUserManager.AddPasswordAsync(identityUser, request.Password);
                    await _identityUserManager.AddToRoleAsync(identityUser, "worker");
                }
                catch (Exception)
                {
                    return new ApiErrorResult<string>("Có lỗi trong quá trình xử lý");
                }

                //create ifo worker
                try
                {
                    var workerInfoDto = new WorkerInfoDto()
                    {
                        UserId = identityUser.Id,
                        Address = request.Address,
                        AddressPoint = request.Address,
                        Gender = request.Gender,
                        AverageRate = request.AverageRate,
                        DateOfBirth = request.DateOfBirth,
                        IdentityCard = request.IdentityCard,
                        Workingime = request.Workingime,
                    };
                    await _workerInfoAppService.AddAsync(workerInfoDto);
                    return new ApiSuccessResult<string>();
                }
                catch (Exception)
                {
                    return new ApiErrorResult<string>("Có lỗi trong quá trình xử lý");
                }
            }
            else
            {
                var workerInfoDto = new WorkerInfoDto()
                {
                    UserId = existUser.Id,
                    Address = request.Address,
                    AddressPoint = request.Address,
                    Gender = request.Gender,
                    AverageRate = request.AverageRate,
                    DateOfBirth = request.DateOfBirth,
                    IdentityCard = request.IdentityCard,
                    Workingime = request.Workingime,
                };
                try
                {
                    await _identityUserManager.AddToRoleAsync(existUser, "worker");
                    await _identityUserManager.RemoveFromRoleAsync(existUser, "customer");
                    await _workerInfoAppService.AddAsync(workerInfoDto);
                    return new ApiSuccessResult<string>();
                }
                catch (Exception)
                {
                    return new ApiErrorResult<string>("Có lỗi trong quá trình xử lý");
                }
            }
        }
    }
}