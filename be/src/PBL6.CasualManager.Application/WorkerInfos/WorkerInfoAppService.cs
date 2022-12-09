using Microsoft.AspNetCore.Mvc;
using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.FileStorages;
using PBL6.CasualManager.JobInfoOfWorkers;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.RateOfWorkers;
using PBL6.CasualManager.TypeOfJobs;
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
        private readonly ICustomerInfoRepository _customerInfoRepository;
        private readonly IJobInfoRepository _jobInfoRepository;
        private readonly IJobInfoOfWorkerRepository _jobInfoOfWorkerRepository;
        private readonly ITypeOfJobRepository _typeOfJobRepository;
        private readonly IRateOfWorkerRepository _rateOfWorkerRepository;

        public WorkerInfoAppService(
            IFileStorageAppService fileStorageAppService,
            IIdentityUserRepository identityUserRepository,
            IWorkerInfoRepository workerInfoRepository,
            IdentityUserManager identityUserManager,
            ICustomerInfoRepository customerInfoRepository,
            IJobInfoRepository jobInfoRepository,
            IJobInfoOfWorkerRepository jobInfoOfWorkerRepository,
            ITypeOfJobRepository typeOfJobRepository,
            IRateOfWorkerRepository rateOfWorkerRepository) : base(workerInfoRepository)
        {
            _fileStorageAppService = fileStorageAppService;
            _workerInfoRepository = workerInfoRepository;
            _identityUserManager = identityUserManager;
            _identityUserRepository = identityUserRepository;
            _jobInfoRepository = jobInfoRepository;
            _jobInfoOfWorkerRepository = jobInfoOfWorkerRepository;
            _rateOfWorkerRepository = rateOfWorkerRepository;
            _typeOfJobRepository = typeOfJobRepository;
            _customerInfoRepository = customerInfoRepository;
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

        //get list worker top 10 rate and in specific province
        [HttpGet]
        [Route("api/app/worker-info/get-list-worker-info-for-mobile/{customerId}")]
        public async Task<ApiResult<List<WorkerInfoResponse>>> GetListWokerToShowInHomePage(Guid customerId)
        {
            try
            {
                var listCustomerInfo = await _customerInfoRepository.GetListAsync();
                var userInfo = listCustomerInfo.FirstOrDefault(x => x.UserId == customerId) ?? new CustomerInfo() { WardId = "", DistrictId = "", ProvinceId = "" };
                var listWorkerInfo = await _workerInfoRepository.GetListAsync();
                var listIdentityUser = await _identityUserRepository.GetListAsync();
                var listJobInfo = await _jobInfoRepository.GetListAsync();
                var listJobInfoOfWorker = await _jobInfoOfWorkerRepository.GetListAsync();
                var listRateOfWorker = await _rateOfWorkerRepository.GetListAsync();
                var listTypeOfJob = await _typeOfJobRepository.GetListAsync();
                var result = (from workerInfo in listWorkerInfo
                              join user in listIdentityUser on workerInfo.UserId equals user.Id
                              select new WorkerInfoResponse()
                              {
                                  Id = workerInfo.UserId,
                                  DistrictId = workerInfo.DistrictId,
                                  ProvinceId = workerInfo.ProvinceId,
                                  WardId = workerInfo.WardId,
                                  Address = workerInfo.Address,
                                  LinkIMG = String.IsNullOrEmpty(workerInfo.Avatar) ? Constants.ImageDefaultWorker : workerInfo.Avatar,
                                  WorkerStatus = workerInfo.Status,
                                  WorkingTime = workerInfo.StartWorkingTime + " - " + workerInfo.EndWorkingTime,
                                  Phone = user.PhoneNumber,
                                  Name = user.Name,
                                  TotalReviews = (from rateOfWorker in listRateOfWorker
                                                  where rateOfWorker.WorkerId == workerInfo.Id
                                                  select rateOfWorker).Count(),
                                  ListJobInfo = ((from jobInfoOfWoker in listJobInfoOfWorker
                                                  join jobInfo in listJobInfo on jobInfoOfWoker.JobInfoId equals jobInfo.Id
                                                  join typeOfJob in listTypeOfJob on jobInfo.TypeOfJobId equals typeOfJob.Id
                                                  where jobInfoOfWoker.WorkerId == workerInfo.Id
                                                  select new JobInfoResponse()
                                                  {
                                                      Id = jobInfo.Id,
                                                      Name = jobInfo.Name,
                                                      Price = jobInfo.Prices,
                                                      Description = jobInfo.Description,
                                                      TypeOfJobId = typeOfJob.Id,
                                                      TypeOfJobName = typeOfJob.Name,
                                                      Image = typeOfJob.Avatar
                                                  }).ToList()),
                                  RateDetail = CreateRateOfWorkerDetailFrom((from rateOfWorker
                                                                             in listRateOfWorker
                                  where rateOfWorker.WorkerId == workerInfo.Id
                                                                             select rateOfWorker).ToList()) ?? new RateOfWorkerResponse()
                                                                             {
                                                                                 RateAverage = 0,
                                                                                 AttitudeRateAverage = 0,
                                                                                 PleasureRateAverage = 0,
                                                                                 SkillRateAverage = 0
                                                                             },
                              }).OrderByDescending(x => (x.WardId.Contains(userInfo.WardId) || x.DistrictId.Contains(userInfo.DistrictId)) ? 0 : 1)
                              .ThenByDescending(x => x.TotalReviews)
                              .Take(10).ToList();

                return new ApiSuccessResult<List<WorkerInfoResponse>>(resultObj: result);
            }
            catch (Exception)
            {
                return new ApiErrorResult<List<WorkerInfoResponse>>(message: "Đã xảy ra lỗi khi lấy dữ liệu!");
            }
        }

        private RateOfWorkerResponse CreateRateOfWorkerDetailFrom(List<RateOfWorker> rateOfWorkers)
        {
            int len = rateOfWorkers.Count();
            if (rateOfWorkers is null || len == 0)
            {
                return null;
            }
            int rateAttitude = 0;
            int rateSkill = 0;
            int ratePleasure = 0;
            foreach (var rateOfWorker in rateOfWorkers)
            {
                rateAttitude += rateOfWorker.AttitudeRate;
                rateSkill += rateOfWorker.SkillRate;
                ratePleasure += rateOfWorker.PleasureRate;
            }
            return new RateOfWorkerResponse()
            {
                AttitudeRateAverage = (float)rateAttitude / len,
                PleasureRateAverage = (float)ratePleasure / len,
                SkillRateAverage = (float)rateSkill / len,
                RateAverage = (rateAttitude + ratePleasure + rateSkill) / (3 * len)
            };
        }
    }
}
