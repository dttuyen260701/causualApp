using Volo.Abp.Modularity;

namespace PBL6.CasualManager;

[DependsOn(
    typeof(CasualManagerApplicationModule),
    typeof(CasualManagerDomainTestModule)
    )]
public class CasualManagerApplicationTestModule : AbpModule
{

}
