using AutoMapper;
using PBL6.CasualManager.Accounts;
using PBL6.CasualManager.Addresses;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Districts;
using PBL6.CasualManager.Extensions;
using PBL6.CasualManager.JobInfoOfWorkers;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.LookupValues;
using PBL6.CasualManager.Oders;
using PBL6.CasualManager.Orders;
using PBL6.CasualManager.Plannings;
using PBL6.CasualManager.PostOfDemands;
using PBL6.CasualManager.Provinces;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.Wards;
using PBL6.CasualManager.WorkerInfos;
using System;
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
                act => act.MapFrom(src => $"{src.Address}, {src.WardName}, {src.DistrictName}, {src.ProvinceName}"))
            .ForMember(des => des.CountOfOrder,
                act => act.MapFrom(src => src.Orders == null ? 0 : src.Orders.Count))
            .ForMember(des => des.Avatar,
                act => act.MapFrom(src => src.Avatar.IsNullOrWhiteSpace() ? "/upload_images/customer/profile.png" : src.Avatar));
        CreateMap<CustomerInfoCreateUpdateDto, CustomerInfo>();
        CreateMap<CustomerInfo, LookupValueDto>();

        CreateMap<WorkerInfo, WorkerInfoDto>()
            .ForMember(des => des.GenderName,
                act => act.MapFrom(src => EnumExtensions.GetDisplayName(src.Gender)))
            .ForMember(des => des.AddressDetail,
                act => act.MapFrom(src => $"{src.Address}, {src.WardName}, {src.DistrictName}, {src.ProvinceName}"))
            .ForMember(des => des.CountOfRating,
                act => act.MapFrom(src => src.RateOfWorkers == null ? 0 : src.RateOfWorkers.Count))
            .ForMember(des => des.Avatar,
                act => act.MapFrom(src => src.Avatar.IsNullOrWhiteSpace() ? "/upload_images/customer/profile.png" : src.Avatar));
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

        CreateMap<Order, OrderDto>()
            .ForMember(des => des.JobName,
                act => act.MapFrom(src => src.JobInfo.Name))
            .ForMember(des => des.FeeForWorker,
                act => act.MapFrom(src => src.PrieceDetail.FeeForWorker))
            .ForMember(des => des.OrderDate,
                act => act.MapFrom(src => src.CreationTime))
            .ForMember(des => des.IconTypeOfJob,
                act => act.MapFrom(src => src.JobInfo.TypeOfJob.Avatar))
            .ForMember(des => des.StatusStr,
                act => act.MapFrom(src => EnumExtensions.GetDisplayName(src.Status)));

        CreateMap<PostOfDemand, PostOfDemandDto>()
            .ForMember(des => des.JobName,
                act => act.MapFrom(src => src.JobInfo.Name))
            .ForMember(des => des.CustomerAddressDetail,
                act => act.MapFrom(src => $"{src.CustomerInfo.DistrictName}, {src.CustomerInfo.ProvinceName}"));

        CreateMap<Planning, PlanningDto>();
        CreateMap<PagedResultDto<Planning>, PagedResultDto<PlanningDto>>();

        CreateMap<Province, ProvinceDto>();
        CreateMap<District, DistrictDto>();
        CreateMap<Ward, WardDto>();
    }
}
