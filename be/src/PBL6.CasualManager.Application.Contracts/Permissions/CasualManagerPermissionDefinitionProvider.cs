using PBL6.CasualManager.Localization;
using Volo.Abp.Authorization.Permissions;
using Volo.Abp.Localization;

namespace PBL6.CasualManager.Permissions;

public class CasualManagerPermissionDefinitionProvider : PermissionDefinitionProvider
{
    public override void Define(IPermissionDefinitionContext context)
    {
        var myGroup = context.AddGroup(CasualManagerPermissions.GroupName);
        //Define your own permissions here. Example:
        //myGroup.AddPermission(CasualManagerPermissions.MyPermission1, L("Permission:MyPermission1"));
    }

    private static LocalizableString L(string name)
    {
        return LocalizableString.Create<CasualManagerResource>(name);
    }
}
