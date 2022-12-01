using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.TypeOfJobs;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;
using static PBL6.CasualManager.Permissions.CasualManagerPermissions;

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
        public async Task<ApiResult<List<JobInfoResponse>>> GetAllJobInfoResponseAsync()
        {
            try
            {
                var jobInfos = await _jobInfoRepository.GetListAsync(includeDetails: true);
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

        public async Task<ApiResult<List<JobInfoResponse>>> GetListJobInfoResponseBelongToTypeOfJobAsync(Guid id)//id = idTypeOfJob
        {
            try
            {
                var jobInfos = await _jobInfoRepository.GetListAsync(x => x.TypeOfJobId == id, includeDetails: true);
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
    }
}
