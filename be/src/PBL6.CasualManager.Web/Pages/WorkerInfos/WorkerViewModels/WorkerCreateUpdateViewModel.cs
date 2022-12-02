using Microsoft.AspNetCore.Mvc;
using PBL6.CasualManager.Enum;
using System.ComponentModel.DataAnnotations;
using System;

namespace PBL6.CasualManager.Web.Pages.WorkerInfos.WorkerViewModels
{
    public class WorkerCreateUpdateViewModel
    {
        [HiddenInput]
        public Guid Id { get; set; }

        [Required]
        [Display(Name = "Worker:Name")]
        public string Name { get; set; }

        [Required]
        [Display(Name = "Common:Username")]
        public string UserName { get; set; }

        [Required]
        [Display(Name = "Common:Gender")]
        public Gender Gender { get; set; }

        [Required]
        [RegularExpression(@"^\$?\d+(\.(\d{2}))?$")]
        [MaxLength(10)]
        [Display(Name = "Common:Phone")]
        public string Phone { get; set; }

        [Required]
        [EmailAddress]
        [Display(Name = "Common:Email")]
        public string Email { get; set; }

        [Required]
        [Display(Name = "Common:Address")]
        public string Address { get; set; }

        [Required]
        [DataType(DataType.Date)]
        [Display(Name = "Common:DateOfBirth")]
        public DateTime DateOfBirth { get; set; } = DateTime.Now;

        [Required]
        [Display(Name = "Common:Province")]
        public string ProvinceId { get; set; }

        public string ProvinceName { get; set; }

        [Required]
        [Display(Name = "Common:District")]
        public string DistrictId { get; set; }

        public string DistrictName { get; set; }

        [Required]
        [Display(Name = "Common:Ward")]
        public string WardId { get; set; }

        public string WardName { get; set; }

        public string Avatar { get; set; }

        [Required]
        [MaxLength(12)]
        [Display(Name = "Worker:IdentityCard")]
        public string IdentityCard { get; set; }

        [Required]
        [DataType(DataType.Date)]
        [Display(Name = "Worker:IdentityCardDate")]
        public DateTime IdentityCardDate { get; set; } = DateTime.Now;

        [Required]
        [MaxLength(256)]
        [Display(Name = "Worker:IdentityCardBy")]
        public string IdentityCardBy { get; set; }

        [Required]
        [DataType(DataType.Time)]
        [Display(Name = "Worker:StartWorkingTime")]
        public string StartWorkingTime { get; set; }

        [Required]
        [DataType(DataType.Time)]
        [Display(Name = "Worker:EndWorkingTime")]
        public string EndWorkingTime { get; set; }

        [Display(Name = "Worker:IsActive")]
        public bool IsActive { get; set; } = true;
    }
}
