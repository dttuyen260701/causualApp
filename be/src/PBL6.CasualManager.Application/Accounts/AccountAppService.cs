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
        private readonly IWorkerInfoRepository _workerInfoRepository;
        private readonly ICustomerInfoRepository _customerInfoRepository;
        private readonly IIdentityUserRepository _identityUserRepository;

        public AccountAppService(IWorkerInfoRepository workerInfoRepository, IIdentityUserRepository identityUserRepository,
            ICustomerInfoRepository customerInfoRepository, IdentityUserManager identityUserManager)
        {
            _customerInfoRepository = customerInfoRepository;
            _workerInfoRepository = workerInfoRepository;
            _identityUserManager = identityUserManager;
            _identityUserRepository = identityUserRepository;
        }

        public async Task<ApiResult<string>> RegisterAsCustomer(RegisterCustomerRequest request)
        {
            bool checkError = false;
            //List<string> arrErr = new List<string>();
            string message = "";
            Dictionary<string, string> arrErr = new Dictionary<string, string>();
            if (await _identityUserManager.FindByEmailAsync(request.Email) != null)
            {
                checkError = true;
                arrErr["email"] = request.Email + " đã tồn tại!";
                message = message +  request.Email + " đã tồn tại. ";
                //arrErr.Add(request.Email + " đã tồn tại!");
            }
            if (await _identityUserRepository.GetCountAsync(phoneNumber: request.Phone) > 0)
            {
                checkError = true;
                arrErr["phone"] = request.Phone + " đã tồn tại!";
                message = message + request.Phone + " đã tồn tại. ";
            }
            if (checkError == true)
            {
                return new ApiErrorResult<string>(arrErr, message);
            }

            //create info identity user
            var identityUser = new IdentityUser(Guid.NewGuid(), request.Email, request.Email);
            identityUser.SetPhoneNumber(request.Phone, true);
            identityUser.Name = request.Name;
            try
            {
                var result = await _identityUserManager.CreateAsync(identityUser, request.Password);
                if(!result.Succeeded)
                {
                    return new ApiErrorResult<string>("Vui lòng kiểm tra lại thông tin");
                }    
                var resultAssignRole = await _identityUserManager.AddToRoleAsync(identityUser, "customer");

            }
            catch (Exception)
            {
                return new ApiErrorResult<string>("Có lỗi trong quá trình xử lý");
            }

            //create info customer
            try
            {
                var customerInfo = new CustomerInfo()
                {
                    UserId = identityUser.Id,
                    Address = request.Address,
                    AddressPoint = request.Address,
                    Gender = request.Gender,
                };
                await _customerInfoRepository.InsertAsync(customerInfo, true);
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
                var identityUser = new IdentityUser(Guid.NewGuid(), request.Email, request.Email);
                identityUser.SetPhoneNumber(request.Phone, true);
                identityUser.Name = request.Name;
                try
                {
                    var result = await _identityUserManager.CreateAsync(identityUser, request.Password);
                    if (!result.Succeeded)
                    {
                        return new ApiErrorResult<string>("Vui lòng kiểm tra lại thông tin");
                    }
                    await _identityUserManager.AddToRoleAsync(identityUser, "worker");
                }
                catch (Exception)
                {
                    return new ApiErrorResult<string>("Có lỗi trong quá trình xử lý");
                }

                //create info worker
                try
                {
                    var workerInfo = new WorkerInfo()
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
                    await _workerInfoRepository.InsertAsync(workerInfo, true);
                    return new ApiSuccessResult<string>();
                }
                catch (Exception)
                {
                    return new ApiErrorResult<string>("Có lỗi trong quá trình xử lý");
                }
            }
            else
            {
                var workerInfo = new WorkerInfo()
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
                    await _workerInfoRepository.InsertAsync(workerInfo, true);
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