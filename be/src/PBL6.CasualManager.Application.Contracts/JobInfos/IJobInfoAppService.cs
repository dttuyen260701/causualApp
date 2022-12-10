using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.LookupValues;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;

namespace PBL6.CasualManager.JobInfos
{
    public interface IJobInfoAppService :
        ICrudAppService<
            JobInfoDto,
            Guid,
            JobInfoConditionSearchDto,
            JobInfoCreateUpdateDto>,
        ILookupValueService
    {
        /// <summary>
        /// Get all JobInfoDtos filter by Job name, TypeOfJob name and TypeOfJobId
        /// <para>Author: Huuy
        /// <para>Created: 11/08/2022
        /// </summary>
        /// <param name="condition">Include FilterName and FilterTypeOfJob</param>
        /// <returns>List JobInfoDto</returns>
        Task<PagedResultDto<JobInfoDto>> GetListSearchAsync(JobInfoConditionSearchDto condition);

        Task<ApiResult<List<JobInfoResponse>>> GetAllJobInfoResponseAsync();

        Task<ApiResult<List<JobInfoResponse>>> GetListJobInfoResponseBelongToTypeOfJobAsync(Guid id);

        Task<List<JobInfoDto>> GetListByTypeOfJobAsync(Guid typeOfJobId);
    }
}
