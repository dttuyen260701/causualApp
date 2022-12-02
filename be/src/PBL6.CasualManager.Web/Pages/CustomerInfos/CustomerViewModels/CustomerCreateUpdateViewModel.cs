using Microsoft.AspNetCore.Mvc;
using PBL6.CasualManager.Enum;
using System;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;

namespace PBL6.CasualManager.Web.Pages.CustomerInfos.CustomerViewModels
{
    public class CustomerCreateUpdateViewModel
    {
        [HiddenInput]
        public Guid Id { get; set; }

        [Required]
        [Display(Name = "Customer:Name")]
        public string Name { get; set; }

        [Required]
        [Display(Name = "Common:Username")]
        public string UserName { get; set; }

        [Required]
        [Display(Name = "Common:Gender")]
        public Gender Gender { get; set; }

        [Required]
        [RegularExpression(@"^\$?\d+(\.(\d{2}))?$")]
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
    }
}
