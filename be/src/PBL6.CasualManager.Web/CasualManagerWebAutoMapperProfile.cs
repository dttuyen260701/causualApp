using AutoMapper;
using Microsoft.AspNetCore.Mvc.Rendering;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.LookupValues;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.Web.Pages.CustomerInfos.CustomerViewModels;
using PBL6.CasualManager.Web.Pages.TypeOfJobs.TypeOfJobViewModels;
using PBL6.CasualManager.Web.Pages.WorkerInfos.WorkerViewModels;
using PBL6.CasualManager.WorkerInfos;
using static PBL6.CasualManager.Web.Pages.CustomerInfos.CreateModalModel;
using static PBL6.CasualManager.Web.Pages.JobInfos.CreateModalModel;
using static PBL6.CasualManager.Web.Pages.JobInfos.EditModalModel;
using static PBL6.CasualManager.Web.Pages.WorkerInfos.CreateModalModel;
using static PBL6.CasualManager.Web.Pages.WorkerInfos.EditModalModel;

namespace PBL6.CasualManager.Web;

public class CasualManagerWebAutoMapperProfile : Profile
{
    public CasualManagerWebAutoMapperProfile()
    {
        //Define your AutoMapper configuration here for the Web project.
        CreateMap<LookupValueDto, SelectListItem>()
                .ForMember(des => des.Value, opt => opt.MapFrom(src => src.Id.ToString()))
                .ForMember(des => des.Text, opt => opt.MapFrom(src => src.Name));

        CreateMap<TypeOfJobCreateUpdateViewModel, TypeOfJobCreateUpdateDto>();
        CreateMap<TypeOfJobDto, TypeOfJobCreateUpdateViewModel>();

        CreateMap<JobInfoCreateViewModel, JobInfoCreateUpdateDto>();
        CreateMap<JobInfoUpdateViewModel, JobInfoCreateUpdateDto>();
        CreateMap<JobInfoDto, JobInfoUpdateViewModel>();

        CreateMap<CustomerCreateViewModel, CustomerInfoCreateUpdateDto>();
        CreateMap<CustomerCreateUpdateViewModel, CustomerInfoCreateUpdateDto>();
        CreateMap<CustomerInfoDto, CustomerCreateUpdateViewModel>();

        CreateMap<WorkerCreateViewModel, WorkerInfoCreateUpdateDto>();
        CreateMap<WorkerInfoDto, WorkerCreateUpdateViewModel>();
        CreateMap<WorkerInfoDto, UpdateWorkerViewModel>();
        CreateMap<UpdateWorkerViewModel, WorkerInfoCreateUpdateDto>();
    }
}
