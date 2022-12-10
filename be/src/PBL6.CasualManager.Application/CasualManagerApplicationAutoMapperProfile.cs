using AutoMapper;
using PBL6.CasualManager.Accounts;
using PBL6.CasualManager.Addresses;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Districts;
using PBL6.CasualManager.Extensions;
using PBL6.CasualManager.JobInfoOfWorkers;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.LookupValues;
using PBL6.CasualManager.Provinces;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.Wards;
using PBL6.CasualManager.WorkerInfos;
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
                act => act.MapFrom(src => src.TypeOfJob.Name))
            .ForMember(des => des.TypeOfJobIcon,
                act => act.MapFrom(src => src.TypeOfJob.Avatar));
        CreateMap<PagedResultDto<JobInfo>, PagedResultDto<JobInfoDto>>();
        CreateMap<JobInfoCreateUpdateDto, JobInfo>();
        CreateMap<TypeOfJob, LookupValueDto>();

        CreateMap<CustomerInfo, CustomerInfoDto>()
            .ForMember(des => des.GenderName,
                act => act.MapFrom(src => EnumExtensions.GetDisplayName(src.Gender)))
            .ForMember(des => des.AddressDetail,
                act => act.MapFrom(src => $"{src.Address}, {src.WardName}, {src.DistrictName}, {src.ProvinceName}"));
        CreateMap<CustomerInfoCreateUpdateDto, CustomerInfo>();

        CreateMap<WorkerInfo, WorkerInfoDto>()
            .ForMember(des => des.GenderName,
                act => act.MapFrom(src => EnumExtensions.GetDisplayName(src.Gender)))
            .ForMember(des => des.AddressDetail,
                act => act.MapFrom(src => $"{src.Address}, {src.WardName}, {src.DistrictName}, {src.ProvinceName}"));
        CreateMap<WorkerInfoCreateUpdateDto, WorkerInfo>();

        CreateMap<JobInfoOfWorker, JobInfoOfWorkerDto>()
            .ForMember(des => des.JobName,
                act => act.MapFrom(src => src.JobInfo.Name))
            .ForMember(des => des.TypeOfJobName,
                act => act.MapFrom(src => src.JobInfo.TypeOfJob.Name))
            .ForMember(des => des.Prices,
                act => act.MapFrom(src => src.JobInfo.Prices))
            .ForMember(des => des.TypeOfJobId,
                act => act.MapFrom(src => src.JobInfo.TypeOfJobId));
        CreateMap<JobInfoOfWorkerCreateUpdateDto, JobInfoOfWorker>();

        CreateMap<Province, ProvinceDto>();
        CreateMap<District, DistrictDto>();
        CreateMap<Ward, WardDto>();
    }
}
