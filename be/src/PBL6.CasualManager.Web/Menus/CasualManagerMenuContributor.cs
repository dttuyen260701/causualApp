using System.Threading.Tasks;
using PBL6.CasualManager.Localization;
using PBL6.CasualManager.MultiTenancy;
using PBL6.CasualManager.Permissions;
using Volo.Abp.Authorization.Permissions;
using Volo.Abp.Identity.Web.Navigation;
using Volo.Abp.SettingManagement.Web.Navigation;
using Volo.Abp.TenantManagement.Web.Navigation;
using Volo.Abp.UI.Navigation;

namespace PBL6.CasualManager.Web.Menus;

public class CasualManagerMenuContributor : IMenuContributor
{
    public async Task ConfigureMenuAsync(MenuConfigurationContext context)
    {
        if (context.Menu.Name == StandardMenus.Main)
        {
            await ConfigureMainMenuAsync(context);
        }
    }

    private async Task ConfigureMainMenuAsync(MenuConfigurationContext context)
    {
        var administration = context.Menu.GetAdministration();
        var l = context.GetLocalizer<CasualManagerResource>();

        context.Menu.Items.Insert(
            0,
            new ApplicationMenuItem(
                CasualManagerMenus.Home,
                l["Menu:Home"],
                "~/",
                icon: "fas fa-home",
                order: 0
            )
        );

        if (MultiTenancyConsts.IsEnabled)
        {
            administration.SetSubItemOrder(TenantManagementMenuNames.GroupName, 1);
        }
        else
        {
            administration.TryRemoveMenuItem(TenantManagementMenuNames.GroupName);
        }

        administration.SetSubItemOrder(IdentityMenuNames.GroupName, 2);
        administration.SetSubItemOrder(SettingManagementMenuNames.GroupName, 3);

        context.Menu.AddItem(new ApplicationMenuItem(CasualManagerMenus.TypeOfJob, l["Menu:TypeOfJob"], "/TypeOfJobs", icon: "fa fa-bookmark", order: 4).RequirePermissions(CasualManagerPermissions.TypesOfJob.Default));
        context.Menu.AddItem(new ApplicationMenuItem(CasualManagerMenus.JobInfo, l["Menu:JobInfo"], "/JobInfos", icon: "fa fa-bookmark", order: 5).RequirePermissions(CasualManagerPermissions.JobInfo.Default));
        context.Menu.AddItem(new ApplicationMenuItem(CasualManagerMenus.CustomerInfo, l["Menu:CustomerInfo"], "/CustomerInfos", icon: "fa fa-users", order: 6).RequirePermissions(CasualManagerPermissions.CustomerInfo.Default));
        context.Menu.AddItem(new ApplicationMenuItem(CasualManagerMenus.WorkerInfo, l["Menu:WorkerInfo"], "/WorkerInfos", icon: "fa fa-gavel", order: 7).RequirePermissions(CasualManagerPermissions.WorkerInfo.Default));
        context.Menu.AddItem(new ApplicationMenuItem(CasualManagerMenus.PostOfDemand, l["Menu:PostOfDemand"], "/PostOfDemands", icon: "fa fa-pencil-square-o", order: 8).RequirePermissions(CasualManagerPermissions.PostOfDemand.Default));
        context.Menu.AddItem(new ApplicationMenuItem(CasualManagerMenus.Planning, l["Menu:Planning"], "/Plannings", icon: "fa fa-paper-plane", order: 9).RequirePermissions(CasualManagerPermissions.Planning.Default));
    }
}
