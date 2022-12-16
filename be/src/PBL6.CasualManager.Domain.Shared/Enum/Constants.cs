using System.ComponentModel.DataAnnotations;

namespace PBL6.CasualManager.Enum
{
    public class Constants
    {
        public const string LinkToFolderImageWorker = "/upload_images/worker/";
        public const string LinkToFolderImageCustomer = "/upload_images/customer/";
        public const string LinkToFolderIconTypeOfJob = "/upload_images/type_of_job/";
        public const string PrefixAvatarWorker = "AvatarWorker-";
        public const string PrefixAvatarCustomer = "AvatarCustomer-";
        public const string PrefixIconTypeOfJob = "IconTypeOfJob-";
        public const string ImageDefaultWorker = "/upload_images/worker/default.png";
        public const string ImageDefaultCustomer = "/upload_images/customer/default.png";

    }

    public enum WithRole
    {
        WhateverRole,
        Worker,
        Customer,
        Admin
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
        [Display(Name = "Common:OrderStatus:IsInProcess")]
        IsInProcess,
        [Display(Name = "Common:OrderStatus:IsCancel")]
        IsCancel,
        [Display(Name = "Common:OrderStatus:IsComplete")]
        IsComplete,
        IsWaiting,
        IsRejected
    }

    public class Role
    {
        public static string WORKER = "Thợ";
        public static string CUSTOMER = "Khách hàng";
    }
}
