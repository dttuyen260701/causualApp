using PBL6.CasualManager.Accounts;
using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.CustomerInfos;
using System;
using System.Threading.Tasks;
using Volo.Abp.Domain.Entities;
using Volo.Abp.Identity;
using Volo.Abp.Validation;

namespace PBL6.CasualManager.WorkerInfos
{
    public class WorkerInfoAppService : CasualManagerAppService, IWorkerInfoAppService
    {
        private readonly IdentityUserManager _identityUserManager;
        private readonly IWorkerInfoRepository _workerInfoRepository;
        public WorkerInfoAppService(IWorkerInfoRepository workerInfoRepository, IdentityUserManager identityUserManager)
        {
            _workerInfoRepository = workerInfoRepository;
            _identityUserManager = identityUserManager;
        }

        public async Task<bool> AddAsync(WorkerInfoDto workerInfosDto)
        {
            var workerInfo = ObjectMapper.Map<WorkerInfoDto, WorkerInfo>(workerInfosDto);
            try
            {
                await _workerInfoRepository.InsertAsync(workerInfo, true);
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public async Task<ApiResult<WorkerInfoAllDto>> UpdateAsync(Guid id, WorkerInfoUpdateRequest request)
        {
            if (id != request.UserId)
            {
                return new ApiErrorResult<WorkerInfoAllDto>("Có lỗi trong quá trình xử lý");
            }
            var identityUser = await _identityUserManager.GetByIdAsync(id);
            if (identityUser == null)
            {
                return new ApiErrorResult<WorkerInfoAllDto>("Không tìm thấy đối tượng");
            }
            var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(request.UserId);
            if (workerInfo != null)
            {
                try
                {
                    identityUser.Name = request.Name;
                    identityUser.SetPhoneNumber(request.Phone, true);
                    await _identityUserManager.UpdateAsync(identityUser);
                    workerInfo.Gender = request.Gender;
                    workerInfo.Address = request.Address;
                    workerInfo.AddressPoint = request.AddressPoint;
                    workerInfo.AverageRate = request.AverageRate;
                    workerInfo.Workingime = request.Workingime;
                    workerInfo.IdentityCard = request.IdentityCard;
                    workerInfo.DateOfBirth = request.DateOfBirth;
                    await _workerInfoRepository.UpdateAsync(workerInfo);
                    var identityUserAfterUpdate = await _identityUserManager.GetByIdAsync(id);
                    var infoWorkerAfterUpdate = await _workerInfoRepository.GetAsync(workerInfo.Id);
                    var workerInfoAllDto = new WorkerInfoAllDto()
                    {
                        Email = identityUserAfterUpdate.Email,
                        UserId = identityUserAfterUpdate.Id,
                        Username = identityUserAfterUpdate.UserName,
                        Name = identityUserAfterUpdate.Name,
                        Phone = identityUserAfterUpdate.PhoneNumber,
                        Gender = infoWorkerAfterUpdate.Gender,
                        Address = infoWorkerAfterUpdate.Address,
                        AddressPoint = infoWorkerAfterUpdate.AddressPoint,
                        IdentityCard = infoWorkerAfterUpdate.IdentityCard,
                        AverageRate = infoWorkerAfterUpdate.AverageRate,
                        DateOfBirth = infoWorkerAfterUpdate.DateOfBirth,
                        Workingime = infoWorkerAfterUpdate.Workingime
                    };
                    return new ApiSuccessResult<WorkerInfoAllDto>();
                }
                catch (Exception)
                {

                    return new ApiErrorResult<WorkerInfoAllDto>("Có lỗi trong quá trình xử lý");
                }
            }
            return new ApiErrorResult<WorkerInfoAllDto>("Không tìm thấy đối tượng");
        }
    }
}
