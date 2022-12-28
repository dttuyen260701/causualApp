using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.JobInfoOfWorkers;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.PagingModels;
using PBL6.CasualManager.Permissions;
using PBL6.CasualManager.RateOfWorkers;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.WorkerInfos;
using PBL6.CasualManager.WorkerResponses;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Domain.Repositories;
using Volo.Abp.Identity;

namespace PBL6.CasualManager.PostOfDemands
{
    public class PostOfDemandAppService : CasualManagerAppService, IPostOfDemandAppService
    {
        private readonly IPostOfDemandRepository _postOfDemandRepository;
        private readonly IIdentityUserRepository _identityUserRepository;
        private readonly ICustomerInfoRepository _customerInfoRepository;
        private readonly IWorkerInfoRepository _workerInfoRepository;
        private readonly IJobInfoRepository _jobInfoRepository;
        private readonly IWorkerResponseRepository _workerResponseRepository;
        private readonly IJobInfoOfWorkerRepository _jobInfoOfWorkerRepository;
        private readonly IRateOfWorkerRepository _rateOfWorkerRepository;
        private readonly ITypeOfJobRepository _typeOfJobRepository;

        public PostOfDemandAppService(IPostOfDemandRepository postOfDemandRepository,
            ICustomerInfoRepository customerInfoRepository, IWorkerInfoRepository workerInfoRepository,
            IIdentityUserRepository identityUserRepository, IJobInfoRepository jobInfoRepository,
            IWorkerResponseRepository workerResponseRepository, IJobInfoOfWorkerRepository jobInfoOfWorkerRepository,
            IRateOfWorkerRepository rateOfWorkerRepository, ITypeOfJobRepository typeOfJobRepository)
        {
            _postOfDemandRepository = postOfDemandRepository;
            _customerInfoRepository = customerInfoRepository;
            _workerInfoRepository = workerInfoRepository;
            _identityUserRepository = identityUserRepository;
            _jobInfoRepository = jobInfoRepository;
            _workerResponseRepository = workerResponseRepository;
            _jobInfoOfWorkerRepository = jobInfoOfWorkerRepository;
            _rateOfWorkerRepository = rateOfWorkerRepository;
            _typeOfJobRepository = typeOfJobRepository;
        }

        [Authorize(CasualManagerPermissions.PostOfDemand.Default)]
        public async Task<PagedResultDto<PostOfDemandDto>> GetListSearchAsync(PostOfDemandConditionSearchDto condition)
        {
            var result = await _postOfDemandRepository.GetListSearchAsync(
                condition.SkipCount,
                condition.MaxResultCount,
                condition.Sorting,
                condition.FilterCustomer);
            var postOfDemandDtos = new List<PostOfDemandDto>();
            foreach (var item in result.Items)
            {
                var now = DateTime.Now;
                var postOfDemandDto = ObjectMapper.Map<PostOfDemand, PostOfDemandDto>(item);
                var identityCustomer = await _identityUserRepository.GetAsync(item.CustomerInfo.UserId);
                postOfDemandDto.CustomerName = identityCustomer.Name;
                postOfDemandDto.DistanceUpLoad = item.CreationTime.Year == now.Year ?
                    item.CreationTime.Month == now.Month ?
                    item.CreationTime.Day == now.Day ?
                    item.CreationTime.Hour == now.Hour ?
                    item.CreationTime.Minute == now.Minute ?
                        L["Common:SecondAgo", now.Second - item.CreationTime.Second]
                        : L["Common:MinuteAgo", now.Minute - item.CreationTime.Minute]
                        : L["Common:HourAgo", now.Hour - item.CreationTime.Hour]
                        : L["Common:DayAgo", now.Day - item.CreationTime.Day]
                        : L["Common:MonthAgo", now.Month - item.CreationTime.Month]
                        : L["Common:YearAgo", now.Year - item.CreationTime.Year];
                postOfDemandDtos.Add(postOfDemandDto);
            }
            return new PagedResultDto<PostOfDemandDto> { Items = postOfDemandDtos, TotalCount = result.TotalCount};
        }

        [Authorize(CasualManagerPermissions.PostOfDemand.Delete)]
        public async Task DeleteAsync(Guid id)
        {
            await _postOfDemandRepository.DeleteAsync(id);
        }

        [HttpPost]
        [Route("api/app/post-of-demand/{customerId}/create")]
        public async Task<ApiResult<PostOfDemandResponse>> CreatePostOfDemandAsync(Guid customerId, CreatePostOfDemandRequest request)
        {
            try
            {
                if (customerId != request.UserId)
                {
                    return new ApiErrorResult<PostOfDemandResponse>(message: "Lỗi!");
                }
                var customer = await _customerInfoRepository.GetEntityCustomerInfoHaveUserId(customerId);
                if (customer == null)
                {
                    return new ApiErrorResult<PostOfDemandResponse>(message: "Lỗi!");
                }
                var postOfDemand = new PostOfDemand()
                {
                    CreationTime = DateTime.Now,
                    IsActive = true,
                    Description = request.Description,
                    Note = request.Note,
                    CustomerId = customer.Id,
                    EndTime = DateTime.ParseExact(request.EndTime, "dd-MM-yyyy", CultureInfo.GetCultureInfo("tr-TR")),
                    JobInfoId = request.JobInfoId,
                    CustomerAddress = request.Address,
                    CustomerAddressPoint = request.AddressPoint
                };
                var result = await _postOfDemandRepository.InsertAsync(postOfDemand, true);
                if (result == null)
                {
                    return new ApiErrorResult<PostOfDemandResponse>(message: "Tạo không thành công");
                }
                var infoIdentityCustomer = await _identityUserRepository.GetAsync(customer.UserId);
                var jobInfo = await _jobInfoRepository.GetAsync(request.JobInfoId);
                return new ApiSuccessResult<PostOfDemandResponse>(resultObj: new PostOfDemandResponse()
                {
                    Id = result.Id,
                    CreationTime = result.CreationTime.ToString("dd-MM-yyyy"),
                    CustomerId = customer.UserId,
                    CustomerName = infoIdentityCustomer.Name,
                    Description = result.Description,
                    Note = result.Note,
                    JobInfoId = result.JobInfoId,
                    JobInfoName = jobInfo.Name,
                    ImageUser = customer.Avatar,
                    EndDateTime = result.EndTime,
                    EndDateTimeString = result.EndTime.ToString("dd-MM-yyyy"),
                    Address = result.CustomerAddress,
                    AddressPoint = result.CustomerAddressPoint

                });
            }
            catch (Exception)
            {
                return new ApiErrorResult<PostOfDemandResponse>(message: "Tạo không thành công!");
            }
        }

        [HttpGet]
        [Route("api/app/post-of-demand/{workerId}/get-post-for-worker")]
        public async Task<ApiResult<PagedResult<PostOfDemandResponse>>> GetListPostOfDemandForWorkerAsync(Guid workerId, PagingRequest paging)
        {
            try
            {
                var wokerinfo = await _workerInfoRepository.FirstOrDefaultAsync(x => x.UserId == workerId);
                if (wokerinfo is null)
                {
                    return new ApiErrorResult<PagedResult<PostOfDemandResponse>>(message: "Không thể tìm thấy người dùng này!");
                }
                var listCustomerInfo = await _customerInfoRepository.GetListAsync();
                var listPostOfDemand = await _postOfDemandRepository.GetListAsync();
                var listIdentityUser = await _identityUserRepository.GetListAsync();
                var listJobInfo = await _jobInfoRepository.GetListAsync();
                var all = (from postOfDemand in listPostOfDemand
                           join customerInfo in listCustomerInfo on postOfDemand.CustomerId equals customerInfo.Id
                           join identityUser in listIdentityUser on customerInfo.UserId equals identityUser.Id
                           join jobInfo in listJobInfo on postOfDemand.JobInfoId equals jobInfo.Id
                           where postOfDemand.EndTime.Date >= DateTime.Now.Date && postOfDemand.IsActive == true
                           orderby postOfDemand.CreationTime descending
                           select new PostOfDemandResponse()
                           {
                               CustomerId = postOfDemand.CustomerId,
                               CustomerName = identityUser.Name,
                               Description = postOfDemand.Description,
                               EndDateTime = postOfDemand.EndTime,
                               EndDateTimeString = postOfDemand.EndTime.ToString("dd-MM-yyyy"),
                               Id = postOfDemand.Id,
                               JobInfoId = postOfDemand.JobInfoId,
                               JobInfoName = jobInfo.Name,
                               Note = postOfDemand.Note,
                               CreationTime = postOfDemand.CreationTime.ToString("dd-MM-yyyy"),
                               ImageUser = customerInfo.Avatar,
                               AddressPoint = postOfDemand.CustomerAddressPoint,
                               Address = postOfDemand.CustomerAddress
                           })
                              .OrderByDescending(x => x.EndDateTime).ToList();
                var itemFilter = all.Skip((paging.PageIndex - 1) * paging.PageSize).Take(paging.PageSize).ToList();
                var result = new PagedResult<PostOfDemandResponse>()
                {
                    Items = itemFilter,
                    PageIndex = paging.PageIndex,
                    PageSize = paging.PageSize,
                    TotalRecords = all.Count()
                };
                return new ApiSuccessResult<PagedResult<PostOfDemandResponse>>(resultObj: result);
            }
            catch (Exception)
            {
                return new ApiErrorResult<PagedResult<PostOfDemandResponse>>(message: "Có lỗi trong quá trình lấy dữ liệu!");
            }
        }

        [HttpPost]
        [Route("api/app/post-of-demand/create-worker-response")]
        public async Task<ApiResult<WorkerInfoResponse>> CreateWorkerResponsePostOfDemand(WorkerResponseRequest request)
        {
            if (request == null)
            {
                return new ApiErrorResult<WorkerInfoResponse>(message: "Đã xảy ra lỗi");
            }
            try
            {
                var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(request.WorkerId);
                if (workerInfo is null)
                {
                    return new ApiErrorResult<WorkerInfoResponse>(message: "Không thực hiện được thao tác này!");
                }
                var postOfDemand = await _postOfDemandRepository.FirstOrDefaultAsync(x => x.Id == request.PostOfDemandId);
                if (postOfDemand is null)
                {
                    return new ApiErrorResult<WorkerInfoResponse>(message: "Bài đăng đã được xóa");
                }
                if (DateTime.Now.Date > postOfDemand.EndTime.Date)
                {
                    return new ApiErrorResult<WorkerInfoResponse>(message: "Bài đăng đã quá hạn!");
                }
                //var listJobInfoOfWorker = await _jobInfoOfWorkerRepository.GetListAsync(x => x.WorkerId == workerInfo.Id);
                //if(listJobInfoOfWorker.Where(x => x.JobInfoId == postOfDemand.JobInfoId).Count() == 0)
                //{
                //    return new ApiErrorResult<WorkerResponseVm>(message: "Bạn không đăng ký loại công việc này!");
                //}
                var workerResponseExist = await _workerResponseRepository.FirstOrDefaultAsync(x => x.WorkerId == workerInfo.Id && x.PostOfDemandId == postOfDemand.Id);
                if (workerResponseExist != null)
                {
                    return new ApiErrorResult<WorkerInfoResponse>(message: "Bạn đã yêu cầu thực hiện bài đăng này!");
                }
                var workerResponse = new WorkerResponse()
                {
                    WorkerId = workerInfo.Id,
                    PostOfDemandId = request.PostOfDemandId,
                };
                var result = await _workerResponseRepository.InsertAsync(workerResponse);
                if (result != null)
                {
                    var infoIdentityWorker = await _identityUserRepository.GetAsync(request.WorkerId);
                    var listJobInfoOfWorker = await _jobInfoOfWorkerRepository.GetListAsync(x => x.WorkerId == workerInfo.Id);
                    var listTypeOfJob = await _typeOfJobRepository.GetListAsync();
                    var listJobInfo = await _jobInfoRepository.GetListAsync();
                    var listRateOfWorker = await _rateOfWorkerRepository.GetListAsync(x => x.WorkerId == workerInfo.Id);
                    //var workerResponseVm = new WorkerResponseVm()
                    //{
                    //    Id = result.Id,
                    //    Time = result.CreationTime.ToString("dd-MM-yyyy HH:mm")
                    //};
                    var workerInfoResponse = new WorkerInfoResponse()
                    {
                        Id = infoIdentityWorker.Id,
                        Address = workerInfo.Address,
                        DistrictId = workerInfo.DistrictId,
                        ProvinceId = workerInfo.ProvinceId,
                        WardId = workerInfo.WardId,
                        Name = infoIdentityWorker.Name,
                        Phone = infoIdentityWorker.PhoneNumber,
                        WorkerStatus = workerInfo.Status,
                        WorkingTime = workerInfo.StartWorkingTime + " - " + workerInfo.EndWorkingTime,
                        LinkIMG = String.IsNullOrEmpty(workerInfo.Avatar) ? Constants.ImageDefaultWorker : workerInfo.Avatar,
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
                                       }).ToList(),
                        RateDetail = CreateRateOfWorkerDetailFrom(listRateOfWorker),
                        TotalReviews = listRateOfWorker.Count()
                    };
                    return new ApiSuccessResult<WorkerInfoResponse>(resultObj: workerInfoResponse);
                }
                return new ApiErrorResult<WorkerInfoResponse>(message: "Yêu cầu không thành công");
            }
            catch (Exception)
            {
                return new ApiErrorResult<WorkerInfoResponse>(message: "Đã xảy ra lỗi");
            }
        }

        [HttpGet]
        [Route("api/app/post-of-demand/{postOfDemandId}/list-woker-request")]
        public async Task<ApiResult<List<WorkerRequestInPostOfDemandResponse>>> GetListWorkerInPostOfDemand(Guid postOfDemandId)
        {
            try
            {
                var listWorkerResponse = await _workerResponseRepository.GetListAsync(x => x.PostOfDemandId == postOfDemandId);
                var listWorker = await _workerInfoRepository.GetListAsync();
                var listIdentityWorker = await _identityUserRepository.GetListAsync();
                var listJobInfoOfWorker = await _jobInfoOfWorkerRepository.GetListAsync();
                var listJobInfo = await _jobInfoRepository.GetListAsync();
                var listRateOfWorker = await _rateOfWorkerRepository.GetListAsync();
                var listTypeOfJob = await _typeOfJobRepository.GetListAsync();
                var results = (from workerResponse in listWorkerResponse
                               join workerInfo in listWorker on workerResponse.WorkerId equals workerInfo.Id
                               join workerIdentity in listIdentityWorker on workerInfo.UserId equals workerIdentity.Id
                               select new WorkerRequestInPostOfDemandResponse()
                               {
                                   LinkIMG = String.IsNullOrEmpty(workerInfo.Avatar) ? Constants.ImageDefaultWorker : workerInfo.Avatar,
                                   Id = workerIdentity.Id,
                                   Name = workerIdentity.Name,
                                   ListJobInfo = (from jobInfoOfWorker in listJobInfoOfWorker
                                                  join jobInfo in listJobInfo on jobInfoOfWorker.JobInfoId equals jobInfo.Id
                                                  join typeOfJob in listTypeOfJob on jobInfo.TypeOfJobId equals typeOfJob.Id
                                                  where jobInfoOfWorker.WorkerId == workerInfo.Id
                                                  select new JobInfoResponse()
                                                  {
                                                      Id = jobInfo.Id,
                                                      Description = jobInfo.Description,
                                                      Image = typeOfJob.Avatar,
                                                      Price = jobInfo.Prices,
                                                      Name = jobInfo.Name,
                                                      TypeOfJobId = typeOfJob.Id,
                                                      TypeOfJobName = typeOfJob.Name,
                                                  }).ToList(),
                                   TotalReviews = (from rateOfWorker in listRateOfWorker
                                                   where rateOfWorker.WorkerId == workerInfo.Id
                                                   select rateOfWorker).Count(),
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
                               }).ToList();
                return new ApiSuccessResult<List<WorkerRequestInPostOfDemandResponse>>(resultObj: results);
            }
            catch (Exception)
            {
                return new ApiErrorResult<List<WorkerRequestInPostOfDemandResponse>>(message: "Đã xảy ra lỗi trong quá trình lấy dữ liệu");
            }
        }

        [HttpGet]
        [Route("api/app/{customerId}/post-of-demand")]
        public async Task<ApiResult<PagedResult<PostOfDemandResponse>>> GetListPostOfDemandForCustomer(Guid customerId, PagingRequest paging)
        {
            try
            {
                var customerInfo = await _customerInfoRepository.GetEntityCustomerInfoHaveUserId(customerId);
                if (customerInfo == null)
                {
                    return new ApiErrorResult<PagedResult<PostOfDemandResponse>>(message: "Không tìm thấy người dùng!");
                }

                var listPostOfDemand = await _postOfDemandRepository.GetListAsync(x => x.CustomerId == customerInfo.Id && x.IsActive && x.EndTime.Date >= DateTime.Now.Date);
                var result = new List<PostOfDemandResponse>();
                if (listPostOfDemand == null)
                {

                    return new ApiSuccessResult<PagedResult<PostOfDemandResponse>>(resultObj: new PagedResult<PostOfDemandResponse>()
                    {
                        Items = result,
                        PageIndex = paging.PageIndex,
                        PageSize = paging.PageSize,
                        TotalRecords = 0
                    });
                }
                var listJobInfo = await _jobInfoRepository.GetListAsync();
                var all = (from postOfDemand in listPostOfDemand
                           join jobInfo in listJobInfo
                           on postOfDemand.JobInfoId equals jobInfo.Id
                           orderby postOfDemand.CreationTime descending
                           select new PostOfDemandResponse()
                           {
                               Id = postOfDemand.Id,
                               Description = postOfDemand.Description,
                               EndDateTimeString = postOfDemand.EndTime.ToString("dd-MM-yyyy"),
                               JobInfoId = postOfDemand.JobInfoId,
                               Note = postOfDemand.Note,
                               CreationTime = postOfDemand.CreationTime.ToString("dd-MM-yyyy"),
                               JobInfoName = jobInfo.Name,
                               AddressPoint = postOfDemand.CustomerAddressPoint,
                               Address = postOfDemand.CustomerAddress
                           }).ToList();
                result = all.Skip((paging.PageIndex - 1) * paging.PageSize).Take(paging.PageSize).ToList();
                return new ApiSuccessResult<PagedResult<PostOfDemandResponse>>(resultObj: new PagedResult<PostOfDemandResponse>()
                {
                    Items = result,
                    PageIndex = paging.PageIndex,
                    PageSize = paging.PageSize,
                    TotalRecords = all.Count()
                });
            }
            catch (Exception)
            {
                return new ApiErrorResult<PagedResult<PostOfDemandResponse>>(message: "Có lỗi trong quá trình lấy dữ liệu");
            }
        }
        [HttpGet]
        [Route("api/app/post-of-demand/{workerId}/request")]
        public async Task<ApiResult<List<PostOfDemandResponse>>> GetListPostOfDemandWorkerRequest(Guid workerId)
        {
            try
            {
                var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(workerId);
                if (workerInfo == null)
                {
                    return new ApiErrorResult<List<PostOfDemandResponse>>(message: "Không tìm thấy người dùng!");
                }
                var listWorkerResponse = await _workerResponseRepository.GetListAsync(x => x.WorkerId == workerInfo.Id);
                var listCustomerInfo = await _customerInfoRepository.GetListAsync();
                var listIdentityUser = await _identityUserRepository.GetListAsync();
                var listPostOfDemand = await _postOfDemandRepository.GetListAsync();
                var listJobInfo = await _jobInfoRepository.GetListAsync();
                var result = (from workerResponse in listWorkerResponse
                              join postOfDemand in listPostOfDemand on workerResponse.PostOfDemandId equals postOfDemand.Id
                              join customerInfo in listCustomerInfo on postOfDemand.CustomerId equals customerInfo.Id
                              join identityUser in listIdentityUser on customerInfo.UserId equals identityUser.Id
                              join jobInfo in listJobInfo on postOfDemand.JobInfoId equals jobInfo.Id
                              where postOfDemand.IsActive == true
                              orderby workerResponse.CreationTime descending
                              select new PostOfDemandResponse()
                              {
                                  Id = postOfDemand.Id,
                                  Address = postOfDemand.CustomerAddress,
                                  CreationTime = postOfDemand.CreationTime.ToString("dd-MM-yyyy"),
                                  JobInfoId = jobInfo.Id,
                                  JobInfoName = jobInfo.Name,
                                  CustomerId = customerInfo.UserId,
                                  CustomerName = identityUser.Name,
                                  Description = postOfDemand.Description,
                                  EndDateTime = postOfDemand.EndTime,
                                  EndDateTimeString = postOfDemand.EndTime.ToString("dd-MM-yyyy"),
                                  ImageUser = customerInfo.Avatar,
                                  Note = postOfDemand.Note,
                                  AddressPoint = postOfDemand.CustomerAddressPoint,
                              }).ToList();
                return new ApiSuccessResult<List<PostOfDemandResponse>>(resultObj: result);
            }
            catch (Exception)
            {
                return new ApiErrorResult<List<PostOfDemandResponse>>(message: "Có lỗi trong quá trình lấy dữ liệu");
            }
        }

        [HttpGet]
        [Route("api/app/post-of-demand/{id}/detail")]
        public async Task<ApiResult<PostOfDemandResponse>> GetPostOfDemandById(Guid id)
        {
            try
            {
                var postOfDemand = await _postOfDemandRepository.GetAsync(id);
                if (postOfDemand == null)
                {
                    return new ApiErrorResult<PostOfDemandResponse>(message: "Không tìm thấy bài đăng!");
                }
                var customerInfo = await _customerInfoRepository.GetAsync(postOfDemand.CustomerId);
                var customerIdentity = await _identityUserRepository.GetAsync(customerInfo.UserId);
                var jobInfo = await _jobInfoRepository.GetAsync(postOfDemand.JobInfoId);
                var listWorkerResponse = await _workerResponseRepository.GetListAsync(x => x.PostOfDemandId == id);
                var listWorker = await _workerInfoRepository.GetListAsync();
                var listIdentityWorker = await _identityUserRepository.GetListAsync();
                var listJobInfoOfWorker = await _jobInfoOfWorkerRepository.GetListAsync();
                var listJobInfo = await _jobInfoRepository.GetListAsync();
                var listRateOfWorker = await _rateOfWorkerRepository.GetListAsync();
                var listTypeOfJob = await _typeOfJobRepository.GetListAsync();
                var listworkerRequestInPostOfDemandResponse = (from workerResponse in listWorkerResponse
                                                               join workerInfo in listWorker on workerResponse.WorkerId equals workerInfo.Id
                                                               join workerIdentity in listIdentityWorker on workerInfo.UserId equals workerIdentity.Id
                                                               select new WorkerRequestInPostOfDemandResponse()
                                                               {
                                                                   LinkIMG = String.IsNullOrEmpty(workerInfo.Avatar) ? Constants.ImageDefaultWorker : workerInfo.Avatar,
                                                                   Id = workerIdentity.Id,
                                                                   Name = workerIdentity.Name,
                                                                   ListJobInfo = (from jobInfoOfWorker in listJobInfoOfWorker
                                                                                  join jobInfo in listJobInfo on jobInfoOfWorker.JobInfoId equals jobInfo.Id
                                                                                  join typeOfJob in listTypeOfJob on jobInfo.TypeOfJobId equals typeOfJob.Id
                                                                                  where jobInfoOfWorker.WorkerId == workerInfo.Id
                                                                                  select new JobInfoResponse()
                                                                                  {
                                                                                      Id = jobInfo.Id,
                                                                                      Description = jobInfo.Description,
                                                                                      Image = typeOfJob.Avatar,
                                                                                      Price = jobInfo.Prices,
                                                                                      Name = jobInfo.Name,
                                                                                      TypeOfJobId = typeOfJob.Id,
                                                                                      TypeOfJobName = typeOfJob.Name,
                                                                                  }).ToList(),
                                                                   TotalReviews = (from rateOfWorker in listRateOfWorker
                                                                                   where rateOfWorker.WorkerId == workerInfo.Id
                                                                                   select rateOfWorker).Count(),
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
                                                               }).ToList();
                var postOfDemandResponse = new PostOfDemandResponse()
                {
                    Address = postOfDemand.CustomerAddress,
                    AddressPoint = postOfDemand.CustomerAddressPoint,
                    CreationTime = postOfDemand.CreationTime.ToString("dd-MM-yyyy HH-mm"),
                    CustomerId = customerInfo.UserId,
                    CustomerName = customerIdentity.Name,
                    Description = postOfDemand.Description,
                    EndDateTime = postOfDemand.EndTime,
                    EndDateTimeString = postOfDemand.EndTime.ToString("dd-MM-yyyy"),
                    Id = postOfDemand.Id,
                    ImageUser = String.IsNullOrEmpty(customerInfo.Avatar) ? Constants.ImageDefaultCustomer : customerInfo.Avatar,
                    JobInfoId = postOfDemand.JobInfoId,
                    Note = postOfDemand.Note,
                    JobInfoName = jobInfo.Name,
                    ListWorkerRequestInPostOfDemandResponse = listworkerRequestInPostOfDemandResponse
                };
                return new ApiSuccessResult<PostOfDemandResponse>(resultObj: postOfDemandResponse);
            }
            catch (Exception)
            {
                return new ApiErrorResult<PostOfDemandResponse>(message: "Có lỗi trong quá trình lấy dữ liệu!");
            }
        }

        [HttpDelete]
        [Route("api/app/post-of-demand/{workerId}/delete-response/{postOfDemandId}")]
        public async Task<ApiResult<string>> DeleteWorkerReponsePostOfDemand(Guid workerId, Guid postOfDemandId)
        {
            try
            {
                var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(workerId);
                if (workerInfo == null)
                {
                    return new ApiErrorResult<string>(message: "Không tìm thấy thông tin thợ!");
                }
                var postOfDemand = await _postOfDemandRepository.GetAsync(postOfDemandId);
                if (postOfDemand == null)
                {
                    return new ApiErrorResult<string>(message: "Có vẻ bài đăng đã bị gỡ bỏ!");
                }
                var workerResponseExist = await _workerResponseRepository.FirstOrDefaultAsync(x => x.WorkerId == workerInfo.Id && x.PostOfDemandId == postOfDemand.Id);
                if (workerResponseExist == null)
                {
                    return new ApiErrorResult<string>(message: "Bạn chưa yêu cầu thực hiện bài đăng này!");
                }
                await _workerResponseRepository.DeleteAsync(workerResponseExist);
                return new ApiSuccessResult<string>();
            }
            catch (Exception)
            {
                return new ApiErrorResult<string>(message: "Đã có lỗi trong quá trình xóa!");
            }
        }

        [HttpPut]
        [Route("api/app/post-of-demand/{id}/unactive")]
        public async Task<ApiResult<string>> UnactivePostOfDemand(Guid id)
        {
            try
            {
                var postOfDemand = await _postOfDemandRepository.FirstOrDefaultAsync(x => x.Id == id);
                if (postOfDemand is null)
                {
                    return new ApiErrorResult<string>(message: "Không tìm thấy bài đăng!");
                }
                //update status
                postOfDemand.IsActive = false;
                var result = await _postOfDemandRepository.UpdateAsync(postOfDemand);
                if (result is null)
                {
                    return new ApiErrorResult<string>(message: "Hủy bài viết không thành công!");
                }
                return new ApiSuccessResult<string>();
            }
            catch (Exception)
            {
                return new ApiErrorResult<string>(message: "Đã có lỗi trong quá trình cập nhật!");
            }
        }

        private RateOfWorkerResponse CreateRateOfWorkerDetailFrom(List<RateOfWorker> rateOfWorkers)
        {
            int len = rateOfWorkers.Count();
            if (rateOfWorkers is null || len == 0)
            {
                return new RateOfWorkerResponse()
                {
                    AttitudeRateAverage = 0,
                    PleasureRateAverage = 0,
                    RateAverage = 0,
                    SkillRateAverage = 0
                };
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
