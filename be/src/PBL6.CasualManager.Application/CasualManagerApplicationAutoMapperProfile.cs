using AutoMapper;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.LookupValues;
using PBL6.CasualManager.TypeOfJobs;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager;

public class CasualManagerApplicationAutoMapperProfile : Profile
{
    public CasualManagerApplicationAutoMapperProfile()
    {
        CreateMap<TypeOfJobDto, TypeOfJob>();
        CreateMap<TypeOfJob, TypeOfJobDto>();
        CreateMap<PagedResultDto<TypeOfJob>, PagedResultDto<TypeOfJobDto>>();
        CreateMap<TypeOfJobCreateUpdateDto, TypeOfJob>();
        CreateMap<TypeOfJob, LookupValueDto>();

        CreateMap<JobInfoDto, JobInfo>();
        CreateMap<JobInfo, JobInfoDto>()
            .ForMember(des => des.TypeOfJobName,
                act => act.MapFrom(src => src.TypeOfJob.Name));
        CreateMap<PagedResultDto<JobInfo>, PagedResultDto<JobInfoDto>>();
        CreateMap<JobInfoCreateUpdateDto, JobInfo>();
    }
}
