using PBL6.CasualManager.TypeOfJobs;
using System;
using System.Collections.Generic;
using System.Text;
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
            JobInfoCreateUpdateDto>
    {
        /// <summary>
        /// Get all JobInfoDtos filter by Job name, TypeOfJob name and TypeOfJobId
        /// <para>Author: Huuy
        /// <para>Created: 11/08/2022
        /// </summary>
        /// <param name="condition">Include FilterName and FilterTypeOfJob</param>
        /// <returns>List JobInfoDto</returns>
        Task<PagedResultDto<JobInfoDto>> GetListSearchAsync(JobInfoConditionSearchDto condition);
    }
}
