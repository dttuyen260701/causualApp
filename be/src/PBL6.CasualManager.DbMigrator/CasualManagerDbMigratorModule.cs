using PBL6.CasualManager.EntityFrameworkCore;
using Volo.Abp.Autofac;
using Volo.Abp.BackgroundJobs;
using Volo.Abp.Modularity;

namespace PBL6.CasualManager.DbMigrator;

[DependsOn(
    typeof(AbpAutofacModule),
    typeof(CasualManagerEntityFrameworkCoreModule),
    typeof(CasualManagerApplicationContractsModule)
    )]
public class CasualManagerDbMigratorModule : AbpModule
{
    public override void ConfigureServices(ServiceConfigurationContext context)
    {
        Configure<AbpBackgroundJobOptions>(options => options.IsJobExecutionEnabled = false);
    }
}
