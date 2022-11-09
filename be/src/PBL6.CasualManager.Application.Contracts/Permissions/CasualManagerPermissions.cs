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
}
