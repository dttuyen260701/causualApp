using PBL6.CasualManager.Localization;
using Volo.Abp.Authorization.Permissions;
using Volo.Abp.Localization;

namespace PBL6.CasualManager.Permissions;

public class CasualManagerPermissionDefinitionProvider : PermissionDefinitionProvider
{
    public override void Define(IPermissionDefinitionContext context)
    {
        var myGroup = context.AddGroup(CasualManagerPermissions.GroupName);

        var typesOfCertificatePermission = myGroup.AddPermission(CasualManagerPermissions.TypesOfJob.Default, L("Permission:TypesOfJob"));
        typesOfCertificatePermission.AddChild(CasualManagerPermissions.TypesOfJob.Create, L("Permission:Create"));
        typesOfCertificatePermission.AddChild(CasualManagerPermissions.TypesOfJob.Update, L("Permission:Update"));
        typesOfCertificatePermission.AddChild(CasualManagerPermissions.TypesOfJob.Delete, L("Permission:Delete"));

        var jobInfoPermission = myGroup.AddPermission(CasualManagerPermissions.JobInfo.Default, L("Permission:JobInfo"));
        jobInfoPermission.AddChild(CasualManagerPermissions.JobInfo.Create, L("Permission:Create"));
        jobInfoPermission.AddChild(CasualManagerPermissions.JobInfo.Update, L("Permission:Update"));
        jobInfoPermission.AddChild(CasualManagerPermissions.JobInfo.Delete, L("Permission:Delete"));
    }

    private static LocalizableString L(string name)
    {
        return LocalizableString.Create<CasualManagerResource>(name);
    }
}
