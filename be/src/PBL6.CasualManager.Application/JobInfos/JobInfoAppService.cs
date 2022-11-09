using PBL6.CasualManager.TypeOfJobs;
using System;
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

        public JobInfoAppService(IJobInfoRepository jobInfoRepository) : base(jobInfoRepository)
        {
            _jobInfoRepository = jobInfoRepository;
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
    }
}
