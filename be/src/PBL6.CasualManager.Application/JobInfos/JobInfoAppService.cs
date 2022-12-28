using Microsoft.AspNetCore.Mvc;
using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.JobInfoOfWorkers;
using PBL6.CasualManager.LookupValues;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.WorkerInfos;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;

namespace PBL6.CasualManager.JobInfos
{
    public class JobInfoAppService :
        CrudAppService<
            JobInfo,
            JobInfoDto,
            Guid,
            JobInfoConditionSearchDto,
            JobInfoCreateUpdateDto>,
        IJobInfoAppService
    {
        private readonly IJobInfoRepository _jobInfoRepository;
        private readonly IJobInfoOfWorkerRepository _jobInfoOfWorkerRepository;
        private readonly IWorkerInfoRepository _workerInfoRepository;
        private readonly ITypeOfJobRepository _typeOfJobRepository;
        public JobInfoAppService(IJobInfoRepository jobInfoRepository, IJobInfoOfWorkerRepository jobInfoOfWorkerRepository
            , IWorkerInfoRepository workerInfoRepository, ITypeOfJobRepository typeOfJobRepository) : base(jobInfoRepository)
        {
            _jobInfoRepository = jobInfoRepository;
            _jobInfoOfWorkerRepository = jobInfoOfWorkerRepository;
            _workerInfoRepository = workerInfoRepository;
            _typeOfJobRepository = typeOfJobRepository;
        }

        public async Task<PagedResultDto<JobInfoDto>> GetListSearchAsync(JobInfoConditionSearchDto condition)
        {
            if (condition.Sorting.IsNullOrWhiteSpace())
            {
                condition.Sorting = nameof(TypeOfJob.CreationTime);
            }
            var results = await _jobInfoRepository.GetListSearchAsync(
                condition.SkipCount,
                condition.MaxResultCount,
                condition.Sorting,
                condition.FilterName,
                condition.FilterTypeOfJob
            );
            return ObjectMapper.Map<PagedResultDto<JobInfo>, PagedResultDto<JobInfoDto>>(results);
        }
        [HttpGet]
        [Route("api/app/job-info/{workerId}/get-all-job-info")]
        public async Task<ApiResult<List<JobInfoResponse>>> GetAllJobInfoResponseAsync(Guid workerId, string? keyword = "")
        {
            try
            {
                var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(workerId);
                var jobInfos = await _jobInfoRepository.GetListAsync();
                var jobInfoOfWorkers = await _jobInfoOfWorkerRepository.GetListAsync(x => x.WorkerId == workerInfo.Id);
                var typeOfJobs = await _typeOfJobRepository.GetListAsync();
                var jobInfosWorkerRegisted = (from jobInfo in jobInfos
                                              join jobInfoOfWorker in jobInfoOfWorkers on jobInfo.Id equals jobInfoOfWorker.JobInfoId
                                              select jobInfo).ToList();
                jobInfos.RemoveAll(jobInfosWorkerRegisted);
                var result = (from jobInfo in jobInfos
                              join typeOfJob in typeOfJobs
                              on jobInfo.TypeOfJobId equals typeOfJob.Id
                              where jobInfo.Name.ToLower().Contains(keyword?.ToLower() ?? "")
                              select new JobInfoResponse()
                              {
                                  Id = jobInfo.Id,
                                  Description = jobInfo.Description,
                                  Name = jobInfo.Name,
                                  Price = jobInfo.Prices,
                                  TypeOfJobId = jobInfo.TypeOfJobId,
                                  TypeOfJobName = typeOfJob.Name,
                                  Image = typeOfJob.Avatar,
                              }).ToList();
                return new ApiSuccessResult<List<JobInfoResponse>>(resultObj: result);
            }
            catch (Exception)
            {
                return new ApiErrorResult<List<JobInfoResponse>>(message: "Đã xảy ra lỗi trong quá trình lấy dữ liệu");
            }
        }

        [HttpGet]
        [Route("/api/app/job-info/{typeOfJobId}/job-info-vm-belong-to-type-of-job")]
        public async Task<ApiResult<List<JobInfoResponse>>> GetListJobInfoBelongToTypeOfJobAsync(Guid typeOfJobId)
        {
            try
            {
                var jobInfos = await _jobInfoRepository.GetListAsync(x => x.TypeOfJobId == typeOfJobId, includeDetails: true);
                if (jobInfos == null)
                {
                    return new ApiSuccessResult<List<JobInfoResponse>>(resultObj: null);
                }
                var result = jobInfos.Select(x => new JobInfoResponse()
                {
                    Id = x.Id,
                    Description = x.Description,
                    Name = x.Name,
                    Price = x.Prices,
                    TypeOfJobId = x.TypeOfJobId,
                    TypeOfJobName = x.TypeOfJob.Name,
                    Image = x.TypeOfJob.Avatar
                }).ToList();
                return new ApiSuccessResult<List<JobInfoResponse>>(resultObj: result);
            }
            catch (Exception)
            {
                return new ApiErrorResult<List<JobInfoResponse>>(message: "Có lỗi trong quá trình lấy dữ liệu!");
            }
        }

        public async Task<List<JobInfoDto>> GetListByTypeOfJobAsync(Guid typeOfJobId)
        {
            var result = await _jobInfoRepository.GetListByTypeOfJobAsync(typeOfJobId);
            return ObjectMapper.Map<List<JobInfo>, List<JobInfoDto>>(result);
        }

        public async Task<List<LookupValueDto>> GetLookupValuesAsync()
        {
            var jobInfos = await _jobInfoRepository.GetListAsync();
            return ObjectMapper.Map<List<JobInfo>, List<LookupValueDto>>(jobInfos);
        }
    }
}
