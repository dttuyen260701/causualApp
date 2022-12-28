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

        var workerInfoPermission = myGroup.AddPermission(CasualManagerPermissions.WorkerInfo.Default, L("Permission:WorkerInfo"));
        workerInfoPermission.AddChild(CasualManagerPermissions.WorkerInfo.Detail, L("Permission:Detail"));
        workerInfoPermission.AddChild(CasualManagerPermissions.WorkerInfo.Create, L("Permission:Create"));
        workerInfoPermission.AddChild(CasualManagerPermissions.WorkerInfo.Update, L("Permission:Update"));
        workerInfoPermission.AddChild(CasualManagerPermissions.WorkerInfo.Delete, L("Permission:Delete"));
        workerInfoPermission.AddChild(CasualManagerPermissions.WorkerInfo.History, L("Permission:History"));

        var customerInfoPermission = myGroup.AddPermission(CasualManagerPermissions.CustomerInfo.Default, L("Permission:CustomerInfo"));
        customerInfoPermission.AddChild(CasualManagerPermissions.CustomerInfo.Create, L("Permission:Create"));
        customerInfoPermission.AddChild(CasualManagerPermissions.CustomerInfo.Update, L("Permission:Update"));
        customerInfoPermission.AddChild(CasualManagerPermissions.CustomerInfo.Delete, L("Permission:Delete"));
        customerInfoPermission.AddChild(CasualManagerPermissions.CustomerInfo.History, L("Permission:History"));

        var jobInfoOfWorkerPermission = myGroup.AddPermission(CasualManagerPermissions.JobInfoOfWorker.Default, L("Permission:JobInfoOfWorker"));
        jobInfoOfWorkerPermission.AddChild(CasualManagerPermissions.JobInfoOfWorker.Create, L("Permission:Create"));
        jobInfoOfWorkerPermission.AddChild(CasualManagerPermissions.JobInfoOfWorker.Update, L("Permission:Update"));
        jobInfoOfWorkerPermission.AddChild(CasualManagerPermissions.JobInfoOfWorker.Delete, L("Permission:Delete"));

        var orderPermission = myGroup.AddPermission(CasualManagerPermissions.Order.Default, L("Permission:Order"));
        orderPermission.AddChild(CasualManagerPermissions.Order.Create, L("Permission:Create"));
        orderPermission.AddChild(CasualManagerPermissions.Order.Update, L("Permission:Update"));
        orderPermission.AddChild(CasualManagerPermissions.Order.Delete, L("Permission:Delete"));

        var postOfDemand = myGroup.AddPermission(CasualManagerPermissions.PostOfDemand.Default, L("Permission:PostOfDemand"));
        postOfDemand.AddChild(CasualManagerPermissions.PostOfDemand.Create, L("Permission:Create"));
        postOfDemand.AddChild(CasualManagerPermissions.PostOfDemand.Update, L("Permission:Update"));
        postOfDemand.AddChild(CasualManagerPermissions.PostOfDemand.Delete, L("Permission:Delete"));

        var statisic = myGroup.AddPermission(CasualManagerPermissions.Statisic.Default, L("Permission:Statisic"));

        var planning = myGroup.AddPermission(CasualManagerPermissions.Planning.Default, L("Permission:Planning"));
        planning.AddChild(CasualManagerPermissions.Planning.Create, L("Permission:Create"));
        planning.AddChild(CasualManagerPermissions.Planning.Update, L("Permission:Update"));
        planning.AddChild(CasualManagerPermissions.Planning.Delete, L("Permission:Delete"));
    }

    private static LocalizableString L(string name)
    {
        return LocalizableString.Create<CasualManagerResource>(name);
    }
}
