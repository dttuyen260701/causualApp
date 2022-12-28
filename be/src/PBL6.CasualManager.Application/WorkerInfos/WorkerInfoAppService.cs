using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.FileStorages;
using PBL6.CasualManager.JobInfoOfWorkers;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.Orders;
using PBL6.CasualManager.PagingModels;
using PBL6.CasualManager.PrieceDetails;
using PBL6.CasualManager.RateOfWorkers;
using PBL6.CasualManager.TypeOfJobs;
using System;
using System.Collections.Generic;
using System.IO;
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
        private readonly IPrieceDetailRepository _prieceDetailRepository;
        private readonly IHostingEnvironment _hostingEnvironment;
        private readonly IOrderRepository _orderRepository;

        public WorkerInfoAppService(
            IFileStorageAppService fileStorageAppService,
            IIdentityUserRepository identityUserRepository,
            IWorkerInfoRepository workerInfoRepository,
            IdentityUserManager identityUserManager,
            ICustomerInfoRepository customerInfoRepository,
            IJobInfoRepository jobInfoRepository,
            IJobInfoOfWorkerRepository jobInfoOfWorkerRepository,
            ITypeOfJobRepository typeOfJobRepository,
            IRateOfWorkerRepository rateOfWorkerRepository,
            IPrieceDetailRepository prieceDetailRepository,
            IHostingEnvironment hostingEnvironment,
            IOrderRepository orderRepository) : base(workerInfoRepository)
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
            _orderRepository = orderRepository;
            _hostingEnvironment = hostingEnvironment;
            _prieceDetailRepository = prieceDetailRepository;
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

        //get list worker top rate and in specific province
        public async Task<ApiResult<List<WorkerInfoResponse>>> GetListWorkerVm(Guid id)//id = idUser
        {
            try
            {
                var listCustomerInfo = await _customerInfoRepository.GetListAsync();
                var userInfo = listCustomerInfo.FirstOrDefault(x => x.UserId == id) ?? new CustomerInfo() { WardId = "", DistrictId = "", ProvinceId = "" };
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
                              .Take(20).ToList();

                return new ApiSuccessResult<List<WorkerInfoResponse>>(resultObj: result);
            }
            catch (Exception)
            {
                return new ApiErrorResult<List<WorkerInfoResponse>>(message: "lỗi");
            }
        }

        [HttpGet]
        [Route("api/app/worker-info/worker-by-type-of-job")]
        public async Task<ApiResult<PagedResult<WorkerInfoResponse>>> GetListWorkerByJobInfo(Guid idUser, Guid idJobInfo, PagingRequest paging)
        {
            try
            {
                var listCustomerInfo = await _customerInfoRepository.GetListAsync();
                var userInfo = listCustomerInfo.FirstOrDefault(x => x.UserId == idUser) ?? new CustomerInfo() { WardId = "", DistrictId = "", ProvinceId = "" };
                var listWorkerInfo = await _workerInfoRepository.GetListAsync();
                var listIdentityUser = await _identityUserRepository.GetListAsync();
                var listJobInfo = await _jobInfoRepository.GetListAsync();
                var listJobInfoOfWorker = await _jobInfoOfWorkerRepository.GetListAsync();
                var listRateOfWorker = await _rateOfWorkerRepository.GetListAsync();
                var listTypeOfJob = await _typeOfJobRepository.GetListAsync();
                var all = (from workerInfo in listWorkerInfo
                           join user in listIdentityUser on workerInfo.UserId equals user.Id
                           join jobInfoOfWorker in listJobInfoOfWorker on workerInfo.Id equals jobInfoOfWorker.WorkerId
                           join jobInfo in listJobInfo on jobInfoOfWorker.JobInfoId equals jobInfo.Id
                           where jobInfo.Id == idJobInfo
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
                           }).DistinctBy(x => x.Id).OrderByDescending(x => (x.WardId.Contains(userInfo.WardId) || x.DistrictId.Contains(userInfo.DistrictId)) ? 0 : 1)
                              .ThenByDescending(x => x.TotalReviews).ToList();
                var listFilter = all.Skip((paging.PageIndex - 1) * paging.PageSize).Take(paging.PageSize).ToList();
                return new ApiSuccessResult<PagedResult<WorkerInfoResponse>>(resultObj: new PagedResult<WorkerInfoResponse>()
                {
                    Items = listFilter,
                    PageIndex = paging.PageIndex,
                    PageSize = paging.PageSize,
                    TotalRecords = all.Count()
                });
            }
            catch (Exception)
            {
                return new ApiErrorResult<PagedResult<WorkerInfoResponse>>(message: "lỗi");
            }
        }

        [HttpGet]
        [Route("api/app/worker-info/{id}/detail")]
        public async Task<ApiResult<WorkerInfoResponse>> GetWorkerInfoDetail(Guid id)
        {
            try
            {
                var workerInfoIdentity = await _identityUserRepository.GetAsync(id);
                var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(id);
                var listRateOfWorker = await _rateOfWorkerRepository.GetListAsync(x => x.WorkerId == workerInfo.Id);
                var listJobInfo = await _jobInfoRepository.GetListAsync();
                var listJobInfoOfWorker = await _jobInfoOfWorkerRepository.GetListAsync(x => x.WorkerId == workerInfo.Id);
                var listTypeOfJob = await _typeOfJobRepository.GetListAsync();
                return new ApiSuccessResult<WorkerInfoResponse>(resultObj: new WorkerInfoResponse()
                {
                    Id = workerInfo.UserId,
                    Address = workerInfo.Address,
                    DistrictId = workerInfo.DistrictId,
                    WardId = workerInfo.WardId,
                    ProvinceId = workerInfo.ProvinceId,
                    LinkIMG = String.IsNullOrEmpty(workerInfo.Avatar) ? Constants.ImageDefaultWorker : workerInfo.Avatar,
                    Name = workerInfoIdentity.Name,
                    WorkerStatus = workerInfo.Status,
                    Phone = workerInfoIdentity.PhoneNumber,
                    WorkingTime = workerInfo.StartWorkingTime + " - " + workerInfo.EndWorkingTime,
                    TotalReviews = listRateOfWorker.Count(),
                    RateDetail = CreateRateOfWorkerDetailFrom(listRateOfWorker),
                    ListJobInfo = (from jobInfoOfWorker in listJobInfoOfWorker
                                   join jobInfo in listJobInfo on jobInfoOfWorker.JobInfoId equals jobInfo.Id
                                   join typeOfJob in listTypeOfJob on jobInfo.TypeOfJobId equals typeOfJob.Id
                                   select new JobInfoResponse()
                                   {
                                       Id = jobInfo.Id,
                                       Description = jobInfo.Description,
                                       Image = typeOfJob.Avatar,
                                       Price = jobInfo.Prices,
                                       Name = jobInfo.Name,
                                       TypeOfJobId = jobInfo.TypeOfJobId,
                                       TypeOfJobName = typeOfJob.Name
                                   }).ToList()
                });
            }
            catch (Exception)
            {
                return new ApiErrorResult<WorkerInfoResponse>(message: "Có lỗi trong quá trình lấy dữ liệu");
            }
        }

        [HttpGet("api/app/worker-info/{workerId}/totalRevenue-totalJob-totalOrder")]
        public async Task<ApiResult<WorkerInfoRateRevenueTotalJobResponse>> Get_TotalJob_TotalRevenue_TotalOrder(Guid workerId)
        {
            try
            {
                var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(workerId);
                var listJobInfoOfWorker = await _jobInfoOfWorkerRepository.GetListAsync(x => x.WorkerId == workerInfo.Id);
                var listOrderCompleteOfWorker = await _orderRepository.GetListAsync(x => x.WorkerId == workerInfo.Id && x.Status == OrderStatus.IsComplete);
                var listPriceDetail = await _prieceDetailRepository.GetListAsync();
                int totalOrder = listOrderCompleteOfWorker.Count();
                float totalRevenue = (from order in listOrderCompleteOfWorker
                                      join priceDetail in listPriceDetail on order.PrieceDetailId equals priceDetail.Id
                                      select priceDetail).Sum(x => x.FeeForWorker);
                int totalJobOfWorker = listJobInfoOfWorker.Count();
                var result = new WorkerInfoRateRevenueTotalJobResponse()
                {
                    TotalJob = totalJobOfWorker,
                    TotalOrder = totalOrder,
                    TotalRevenue = totalRevenue
                };
                return new ApiSuccessResult<WorkerInfoRateRevenueTotalJobResponse>(resultObj: result);
            }
            catch (Exception)
            {
                return new ApiErrorResult<WorkerInfoRateRevenueTotalJobResponse>(message: "Lỗi");
            }
        }

        [HttpGet]
        [Route("api/app/worker-info/{id}/list-job-info")]
        public async Task<ApiResult<List<JobInfoResponse>>> GetListJobInfoBelongToWoker(Guid id)
        {
            try
            {
                var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(id);
                var listJobInfo = await _jobInfoRepository.GetListAsync();
                var lisTypeOfJob = await _typeOfJobRepository.GetListAsync();
                var listInfoOfWorker = await _jobInfoOfWorkerRepository.GetListAsync(x => x.WorkerId == workerInfo.Id);
                var result = (from jobInfoOfWorker in listInfoOfWorker
                              join jobInfo in listJobInfo on jobInfoOfWorker.JobInfoId equals jobInfo.Id
                              join typeOfJob in lisTypeOfJob on jobInfo.TypeOfJobId equals typeOfJob.Id
                              select new JobInfoResponse()
                              {
                                  Id = jobInfo.Id,
                                  Description = jobInfo.Description,
                                  Image = typeOfJob.Avatar,
                                  Name = jobInfo.Name,
                                  Price = jobInfo.Prices,
                                  TypeOfJobId = jobInfo.TypeOfJobId,
                                  TypeOfJobName = typeOfJob.Name,
                                  Note = jobInfoOfWorker.Note,
                                  CreationTime = jobInfoOfWorker.CreationTime.ToString("dd-MM-yyyy")
                              }).ToList();
                return new ApiSuccessResult<List<JobInfoResponse>>(resultObj: result);
            }
            catch (Exception)
            {
                return new ApiErrorResult<List<JobInfoResponse>>(message: "Lỗi");
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
