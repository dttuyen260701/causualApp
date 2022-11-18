using Microsoft.AspNetCore.Http;
using PBL6.CasualManager.LookupValues;
using System;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;

namespace PBL6.CasualManager.TypeOfJobs
{
    public interface ITypeOfJobAppService :
        ICrudAppService<
            TypeOfJobDto,
            Guid,
            TypeOfJobConditionSearchDto,
            TypeOfJobCreateUpdateDto>,
        ILookupValueService
    {
        /// <summary>
        /// Get all TypeOfJobDtos filter by name
        /// <para>Author: Huuy
        /// <para>Created: 11/08/2022
        /// </summary>
        /// <param name="condition">Include FilterName</param>
        /// <returns>List TypeOfJobDtos</returns>
        Task<PagedResultDto<TypeOfJobDto>> GetListByNameAsync(TypeOfJobConditionSearchDto condition);
    }
}
