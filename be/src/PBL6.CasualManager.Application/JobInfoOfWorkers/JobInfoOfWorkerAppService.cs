using Microsoft.AspNetCore.Mvc;
using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.WorkerInfos;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;
using Volo.Abp.Domain.Repositories;
using Volo.Abp.Identity;
using static PBL6.CasualManager.JobInfoOfWorkers.JobInfoOfWorkerBusinessException;

namespace PBL6.CasualManager.JobInfoOfWorkers
{
    public class JobInfoOfWorkerAppService :
        CrudAppService<
            JobInfoOfWorker,
            JobInfoOfWorkerDto,
            Guid,
            JobInfoOfWorkerConditionSearchDto,
            JobInfoOfWorkerCreateUpdateDto>,
        IJobInfoOfWorkerAppService
    {
        private readonly IJobInfoOfWorkerRepository _jobInfoOfWorkerRepository;
        private readonly IWorkerInfoRepository _workerInfoRepository;
        private readonly IIdentityUserRepository _identityUserRepository;

        public JobInfoOfWorkerAppService(
            IJobInfoOfWorkerRepository jobInfoOfWorkerRepository,
            IIdentityUserRepository identityUserRepository, IWorkerInfoRepository workerInfoRepository) : base(jobInfoOfWorkerRepository)
        {
            _jobInfoOfWorkerRepository = jobInfoOfWorkerRepository;
            _identityUserRepository = identityUserRepository;
            _workerInfoRepository = workerInfoRepository;
        }

        public async Task<PagedResultDto<JobInfoOfWorkerDto>> GetListSearchAsync(JobInfoOfWorkerConditionSearchDto condition)
        {
            if (condition.Sorting.IsNullOrWhiteSpace())
            {
                condition.Sorting = nameof(TypeOfJob.CreationTime);
            }
            var results = await _jobInfoOfWorkerRepository.SearchList(
                condition.SkipCount,
                condition.MaxResultCount,
                condition.Sorting,
                condition.FilterWorker,
                condition.FilterJobName,
                condition.FilterTypeOfJob
            );
            var response = new List<JobInfoOfWorkerDto>();
            foreach(var item in results.Items)
            {
                response.Add(ObjectMapper.Map<JobInfoOfWorker, JobInfoOfWorkerDto>(item));
            }
            return new PagedResultDto<JobInfoOfWorkerDto> { Items = response, TotalCount = results.TotalCount};
        }

        public async Task CreateJobInfoOfWorker(JobInfoOfWorkerCreateUpdateDto jobInfoOfWorkerCreateUpdateDto)
        {
            var jobInfoOfWorker = await _jobInfoOfWorkerRepository.GetByJobInfoAndWorker(
                jobInfoOfWorkerCreateUpdateDto.JobInfoId, jobInfoOfWorkerCreateUpdateDto.WorkerId);
            if (jobInfoOfWorker != null)
            {
                var identityUser = await _identityUserRepository.GetAsync(jobInfoOfWorker.WorkerInfo.UserId);
                throw new IsExistWorkerWithJobInfoException(identityUser.Name, jobInfoOfWorker.JobInfo.Name);
            }
            await base.CreateAsync(jobInfoOfWorkerCreateUpdateDto);
        }

        public async Task UpdateJobInfoOfWorker(Guid id, JobInfoOfWorkerCreateUpdateDto jobInfoOfWorkerCreateUpdateDto)
        {
            var jobInfoOfWorker = await _jobInfoOfWorkerRepository.GetByJobInfoAndWorker(
                jobInfoOfWorkerCreateUpdateDto.JobInfoId, jobInfoOfWorkerCreateUpdateDto.WorkerId);
            if (jobInfoOfWorker != null && id != jobInfoOfWorker.Id)
            {
                var identityUser = await _identityUserRepository.GetAsync(jobInfoOfWorker.WorkerInfo.UserId);
                throw new IsExistWorkerWithJobInfoException(identityUser.Name, jobInfoOfWorker.JobInfo.Name);
            }
            await base.UpdateAsync(id, jobInfoOfWorkerCreateUpdateDto);
        }

        [HttpGet]
        [Route("api/app/job-info-of-worker/{workerId}/del/{jobInfoId}")]
        public async Task<ApiResult<string>> RemoveJobInfoOfWorker(Guid jobInfoId, Guid workerId)
        {
            try
            {
                var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(workerId);
                if (workerInfo == null)
                {
                    return new ApiErrorResult<string>(message: "Không tìm thấy thông tin người dùng!");
                }
                var jobInfoOfWorker = await _jobInfoOfWorkerRepository.FirstOrDefaultAsync(x => x.JobInfoId == jobInfoId && x.WorkerId == workerInfo.Id);
                if (jobInfoOfWorker == null)
                {
                    return new ApiErrorResult<string>(message: "Bạn không đăng ký việc làm này");
                }
                await _jobInfoOfWorkerRepository.DeleteAsync(jobInfoOfWorker);
                return new ApiSuccessResult<string>();
            }
            catch (Exception)
            {
                return new ApiErrorResult<string>(message: "Đã xảy ra lỗi trong quá trình xóa!");
            }
        }

        [HttpPost]
        [Route("api/app/job-info-of-worker/{workerId}/register")]
        public async Task<ApiResult<string>> RegisterJobForWorker(Guid workerId, RegisterJobRequest request)
        {
            if (request == null)
            {
                return new ApiErrorResult<string>(message: "Thông tin đăng ký không hợp lệ!");
            }
            try
            {
                var findJobInfoOfWorker = await _jobInfoOfWorkerRepository.FirstOrDefaultAsync(x => x.WorkerInfo.UserId == workerId && x.JobInfoId == request.JobInfoId);
                if (findJobInfoOfWorker != null)
                {
                    return new ApiErrorResult<string>(message: "Bạn đã đăng ký loại việc làm này!");
                }
                var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(workerId);
                if (workerInfo == null)
                {
                    return new ApiErrorResult<string>(message: "Tài khoản không tồn tại!");
                }
                var jobInfoOfWorker = new JobInfoOfWorker()
                {
                    JobInfoId = request.JobInfoId,
                    WorkerId = workerInfo.Id,
                    Note = request.Note
                };
                var result = await _jobInfoOfWorkerRepository.InsertAsync(jobInfoOfWorker);
                if (result == null)
                {
                    return new ApiErrorResult<string>(message: "Đăng ký không thành công!");
                }
                return new ApiSuccessResult<string>();
            }
            catch (Exception)
            {
                return new ApiErrorResult<string>(message: "Có lỗi trong quá trình đăng ký!");
            }
        }

    }
}
