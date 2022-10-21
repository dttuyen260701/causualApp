using PBL6.CasualManager.EntityFrameworkCore;
using Volo.Abp.Modularity;

namespace PBL6.CasualManager;

[DependsOn(
    typeof(CasualManagerEntityFrameworkCoreTestModule)
    )]
public class CasualManagerDomainTestModule : AbpModule
{

}
