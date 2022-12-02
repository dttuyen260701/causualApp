using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.FileStorages;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;
using Volo.Abp.Identity;
using static PBL6.CasualManager.WorkerInfos.WorkerInfoBusinessException;

namespace PBL6.CasualManager.WorkerInfos
{
    public class WorkerInfoAppService :
        CrudAppService<
            WorkerInfo,
            WorkerInfoDto,
            Guid,
            WorkerInfoConditionSearchDto,
            WorkerInfoCreateUpdateDto>
        , IWorkerInfoAppService
    {
        private readonly IdentityUserManager _identityUserManager;
        private readonly IFileStorageAppService _fileStorageAppService;
        private readonly IWorkerInfoRepository _workerInfoRepository;
        private readonly IIdentityUserRepository _identityUserRepository;
        public WorkerInfoAppService(
            IFileStorageAppService fileStorageAppService,
            IIdentityUserRepository identityUserRepository,
            IWorkerInfoRepository workerInfoRepository,
            IdentityUserManager identityUserManager) : base(workerInfoRepository)
        {
            _fileStorageAppService = fileStorageAppService;
            _workerInfoRepository = workerInfoRepository;
            _identityUserManager = identityUserManager;
            _identityUserRepository = identityUserRepository;
        }

        public async Task<PagedResultDto<WorkerInfoDto>> GetListWorkerAllInfoAsync(WorkerInfoConditionSearchDto condition)
        {
            var listWorkerInfoDto = await _workerInfoRepository.GetListAsync();
            var listWorkerInfoAllDto = new List<WorkerInfoDto>();
            foreach (var worker in listWorkerInfoDto)
            {
                var workerIdentity = await _identityUserRepository.GetAsync(worker.UserId);
                var workerInfoAllDto = ObjectMapper.Map<WorkerInfo, WorkerInfoDto>(worker);
                workerInfoAllDto.Name = workerIdentity.Name;
                workerInfoAllDto.Email = workerIdentity.Email;
                workerInfoAllDto.Phone = workerIdentity.PhoneNumber;
                workerInfoAllDto.UserName = workerIdentity.UserName;
                listWorkerInfoAllDto.Add(workerInfoAllDto);
            }
            listWorkerInfoAllDto = listWorkerInfoAllDto
                .WhereIf(!condition.Keyword.IsNullOrWhiteSpace(),
                    x => x.Name.ToLower().Contains(condition.Keyword.ToLower())).ToList();
            return new PagedResultDto<WorkerInfoDto> { Items = listWorkerInfoAllDto, TotalCount = listWorkerInfoAllDto.Count };
        }

        public async Task<WorkerInfoDto> GetWorkerInfoAsync(Guid id)
        {
            var workerInfo = await _workerInfoRepository.GetAsync(id);
            var workerIdentity = await _identityUserRepository.GetAsync(workerInfo.UserId);
            var result = ObjectMapper.Map<WorkerInfo, WorkerInfoDto>(workerInfo);
            result.Name = workerIdentity.Name;
            result.Email = workerIdentity.Email;
            result.Phone = workerIdentity.PhoneNumber;
            result.UserName = workerIdentity.UserName;
            return result;
        }

        public async Task CreateWorkerInfoAsync(WorkerInfoCreateUpdateDto workerInfoCreateUpdateDto)
        {
            if (await _identityUserManager.FindByEmailAsync(workerInfoCreateUpdateDto.Email) != null)
            {
                throw new EmailExistException(workerInfoCreateUpdateDto.Email);
            }
            if (await _identityUserManager.FindByNameAsync(workerInfoCreateUpdateDto.UserName) != null)
            {
                throw new UserNameExistException(workerInfoCreateUpdateDto.UserName);
            }
            if (await _identityUserRepository.GetCountAsync(phoneNumber: workerInfoCreateUpdateDto.Phone) > 0)
            {
                throw new PhoneExistException(workerInfoCreateUpdateDto.Phone);
            }
            if (await _workerInfoRepository.CheckExistIdentityCard(workerInfoCreateUpdateDto.IdentityCard))
            {
                throw new IdentityCardExistException(workerInfoCreateUpdateDto.IdentityCard);
            }

            var identityUserCreate = new IdentityUser(Guid.NewGuid(), workerInfoCreateUpdateDto.UserName, workerInfoCreateUpdateDto.Email);
            identityUserCreate.SetPhoneNumber(workerInfoCreateUpdateDto.Phone, true);
            identityUserCreate.Name = workerInfoCreateUpdateDto.Name;

            try
            {
                var result = await _identityUserManager.CreateAsync(identityUserCreate, workerInfoCreateUpdateDto.Password);
                if (!result.Succeeded)
                {
                    throw new InfoWrongException();
                }
                var resultAddRole = await _identityUserManager.AddToRoleAsync(identityUserCreate, Role.WORKER);
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

            var workerInfoCreate = ObjectMapper.Map<WorkerInfoCreateUpdateDto, WorkerInfo>(workerInfoCreateUpdateDto);
            workerInfoCreate.UserId = identityUserCreate.Id;
            workerInfoCreate.AddressPoint = "";
            await _workerInfoRepository.InsertAsync(workerInfoCreate);
        }

        public async Task UpdateWorkerInfoAsync(Guid id, WorkerInfoCreateUpdateDto workerInfoCreateUpdateDto)
        {
            var workerInfo = await _workerInfoRepository.GetAsync(id);
            var workerIdentity = await _identityUserRepository.GetAsync(workerInfo.UserId).ConfigureAwait(false);
            if ((await _identityUserRepository.GetCountAsync(phoneNumber: workerInfoCreateUpdateDto.Phone) > 0) && workerInfoCreateUpdateDto.Phone != workerIdentity.PhoneNumber)
            {
                throw new PhoneExistException(workerInfoCreateUpdateDto.Phone);
            }
            workerInfo.Gender = workerInfoCreateUpdateDto.Gender;
            workerInfo.Address = workerInfoCreateUpdateDto.Address;
            workerInfo.AddressPoint = string.Empty;
            workerInfo.DistrictId = workerInfoCreateUpdateDto.DistrictId;
            workerInfo.DistrictName = workerInfoCreateUpdateDto.DistrictName;
            workerInfo.WardId = workerInfoCreateUpdateDto.WardId;
            workerInfo.WardName = workerInfoCreateUpdateDto.WardName;
            workerInfo.ProvinceId = workerInfoCreateUpdateDto.ProvinceId;
            workerInfo.ProvinceName = workerInfoCreateUpdateDto.ProvinceName;
            workerInfo.DateOfBirth = workerInfoCreateUpdateDto.DateOfBirth;
            workerInfo.Avatar = workerInfoCreateUpdateDto.Avatar;
            workerInfo.StartWorkingTime = workerInfoCreateUpdateDto.StartWorkingTime;
            workerInfo.EndWorkingTime = workerInfoCreateUpdateDto.EndWorkingTime;
            workerInfo.Status = workerInfoCreateUpdateDto.Status;
            workerInfo.IsActive = workerInfoCreateUpdateDto.IsActive;
            workerInfo.IdentityCard = workerInfoCreateUpdateDto.IdentityCard;
            workerInfo.IdentityCardDate = workerInfoCreateUpdateDto.IdentityCardDate;
            workerInfo.IdentityCardBy = workerInfoCreateUpdateDto.IdentityCardBy;
            await _workerInfoRepository.UpdateAsync(workerInfo);

            workerIdentity.Name = workerInfoCreateUpdateDto.Name;
            workerIdentity.SetPhoneNumber(workerInfoCreateUpdateDto.Phone, true);
            await _identityUserManager.UpdateAsync(workerIdentity);
        }

        public async Task DeleteWorkerInfoAsync(Guid id)
        {
            var workerInfo = await _workerInfoRepository.GetAsync(id);
            var workerIdentity = await _identityUserRepository.GetAsync(workerInfo.UserId).ConfigureAwait(false);
            var deleteIdentity = await _identityUserManager.DeleteAsync(workerIdentity);
            if (deleteIdentity.Succeeded)
            {
                await _workerInfoRepository.DeleteAsync(workerInfo);
            }
            if (workerInfo.Avatar != null)
            {
                _fileStorageAppService.DeleteImageAsync(workerInfo.Avatar);
            }
        }

    }
}
