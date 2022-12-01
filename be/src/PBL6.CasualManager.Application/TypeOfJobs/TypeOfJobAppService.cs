﻿using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.LookupValues;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;

namespace PBL6.CasualManager.TypeOfJobs
{
    public class TypeOfJobAppService :
        CrudAppService<
            TypeOfJob,
            TypeOfJobDto,
            Guid,
            TypeOfJobConditionSearchDto,
            TypeOfJobCreateUpdateDto>,
        ITypeOfJobAppService
    {
        private readonly ITypeOfJobRepository _typeOfJobRepository;

        public TypeOfJobAppService(ITypeOfJobRepository typeOfJobRepository) : base(typeOfJobRepository)
        {
            _typeOfJobRepository = typeOfJobRepository;
        }

        public async Task<PagedResultDto<TypeOfJobDto>> GetListByNameAsync(TypeOfJobConditionSearchDto condition)
        {
            if (condition.Sorting.IsNullOrWhiteSpace())
            {
                condition.Sorting = nameof(TypeOfJob.CreationTime);
            }
            var results = await _typeOfJobRepository.GetListByName(
                condition.SkipCount,
                condition.MaxResultCount,
                condition.Sorting,
                condition.FilterName
            );
            return ObjectMapper.Map<PagedResultDto<TypeOfJob>, PagedResultDto<TypeOfJobDto>>(results);
        }

        public async Task<List<LookupValueDto>> GetLookupValuesAsync()
        {
            var typeOfJob = await _typeOfJobRepository.GetListAsync();
            return ObjectMapper.Map<List<TypeOfJob>, List<LookupValueDto>>(typeOfJob);
        }

        public async Task<ApiResult<List<TypeOfJobResponse>>> GetAllTypeJobAsync()
        {
            try
            {
                var typeOfJobs = await _typeOfJobRepository.GetListAsync();
                var result = typeOfJobs.Select(x => new TypeOfJobResponse()
                {
                    Id = x.Id,
                    Image = x.Avatar,
                    Name = x.Name,
                    Description = x.Description
                }).ToList();
                return new ApiSuccessResult<List<TypeOfJobResponse>>(result);
            }
            catch (Exception)
            {
                return new ApiErrorResult<List<TypeOfJobResponse>>(message: "Có lỗi trong quá trình lấy dữ liệu!");
            }
        }
    }
}
