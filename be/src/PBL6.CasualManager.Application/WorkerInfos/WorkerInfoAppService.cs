using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PBL6.CasualManager.Accounts;
using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Enum;
using System;
using System.Globalization;
using System.IO;
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
        private readonly IHostingEnvironment _hostingEnvironment;
        private readonly IIdentityUserRepository _identityUserRepository;
        public WorkerInfoAppService(IIdentityUserRepository identityUserRepository, IHostingEnvironment hostingEnvironment, IWorkerInfoRepository workerInfoRepository, IdentityUserManager identityUserManager)
        {
            _workerInfoRepository = workerInfoRepository;
            _identityUserManager = identityUserManager;
            _hostingEnvironment = hostingEnvironment;
            _identityUserRepository = identityUserRepository;
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

        public async Task<ApiResult<WorkerInfoAllDto>> UpdateAsync(Guid id, [FromForm] WorkerInfoUpdateRequest request)
        {
            if (id != request.Id)
            {
                return new ApiErrorResult<WorkerInfoAllDto>("Có lỗi trong quá trình xử lý");
            }
            var identityUser = await _identityUserManager.GetByIdAsync(id);
            if (identityUser == null)
            {
                return new ApiErrorResult<WorkerInfoAllDto>("Không tìm thấy đối tượng");
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
                    
                    var workerInfoAllDto = new WorkerInfoAllDto()
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
                    return new ApiSuccessResult<WorkerInfoAllDto>(resultObj: workerInfoAllDto);
                }
                catch (Exception)
                {

                    return new ApiErrorResult<WorkerInfoAllDto>("Có lỗi trong quá trình xử lý");
                }
            }
            return new ApiErrorResult<WorkerInfoAllDto>("Không tìm thấy đối tượng");
        }
        private async Task<string> SaveImageAsync(IFormFile image, Guid idUser)
        {
            if (image == null)
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
            catch (Exception)
            {
                return null;
            }
        }
    }
}
