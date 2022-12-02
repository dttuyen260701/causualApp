namespace PBL6.CasualManager.Permissions;

public static class CasualManagerPermissions
{
    public const string GroupName = "CasualManager";

    public class TypesOfJob
    {
        public const string Default = GroupName + ".TypesOfJob";
        public const string Update = Default + ".Update";
        public const string Create = Default + ".Create";
        public const string Delete = Default + ".Delete";
    }

    public class JobInfo
    {
        public const string Default = GroupName + ".JobInfo";
        public const string Update = Default + ".Update";
        public const string Create = Default + ".Create";
        public const string Delete = Default + ".Delete";
    }

    public class WorkerInfo
    {
        public const string Default = GroupName + ".WorkerInfo";
        public const string Update = Default + ".Update";
        public const string Create = Default + ".Create";
        public const string Delete = Default + ".Delete";
    }

    public class CustomerInfo
    {
        public const string Default = GroupName + ".CustomerInfo";
        public const string Update = Default + ".Update";
        public const string Create = Default + ".Create";
        public const string Delete = Default + ".Delete";
    }
}
