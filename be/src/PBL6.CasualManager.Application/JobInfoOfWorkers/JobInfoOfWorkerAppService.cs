using PBL6.CasualManager.TypeOfJobs;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;
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
        private readonly IIdentityUserRepository _identityUserRepository;

        public JobInfoOfWorkerAppService(
            IJobInfoOfWorkerRepository jobInfoOfWorkerRepository,
            IIdentityUserRepository identityUserRepository) : base(jobInfoOfWorkerRepository)
        {
            _jobInfoOfWorkerRepository = jobInfoOfWorkerRepository;
            _identityUserRepository = identityUserRepository;
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

    }
}
