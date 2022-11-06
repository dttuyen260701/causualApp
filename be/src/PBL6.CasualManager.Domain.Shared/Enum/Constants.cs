using System.ComponentModel.DataAnnotations;

namespace PBL6.CasualManager.Enum
{
    public class Constants
    {
    }

    public enum Gender
    {
        [Display(Name = "Common:Gender:Male")]
        Male,
        [Display(Name = "Common:Gender:FeMale")]
        Female,
        [Display(Name = "Common:Gender:Undefined")]
        Undefined
    }

    public enum WorkerStatus
    {
        Free,
        Busy
    }

    public enum OrderStatus
    {
        IsInProcess,
        Complete
    }

    public class Role
    {
        public static string WORKER = "Thợ";
        public static string CUSTOMER = "Khách hàng";
    }
}
