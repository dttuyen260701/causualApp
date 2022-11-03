using AutoMapper;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.WorkerInfos;
using Volo.Abp.Identity;

namespace PBL6.CasualManager;

public class CasualManagerApplicationAutoMapperProfile : Profile
{
    public CasualManagerApplicationAutoMapperProfile()
    {
        /* You can configure your AutoMapper mapping configuration here.
         * Alternatively, you can split your mapping configurations
         * into multiple profile classes for a better organization. */
        CreateMap<CustomerInfo, CustomerInfoDto>().ReverseMap();
        CreateMap<WorkerInfo, WorkerInfoDto>().ReverseMap();

    }
}
