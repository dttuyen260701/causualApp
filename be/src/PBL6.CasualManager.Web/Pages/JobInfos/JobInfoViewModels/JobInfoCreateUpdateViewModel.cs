using System.ComponentModel.DataAnnotations;
using System;
using Microsoft.AspNetCore.Mvc;

namespace PBL6.CasualManager.Web.Pages.JobInfos.JobInfoViewModels
{
    public class JobInfoCreateUpdateViewModel
    {
        [HiddenInput]
        public Guid Id { get; set; }

        [Required]
        [Display(Name = "JobInfo:Name")]
        public string Name { get; set; }

        [Required]
        [Display(Name = "JobInfo:Description")]
        public string Description { get; set; }

        [Required]
        [Display(Name = "JobInfo:Prices")]
        public int Prices { get; set; }

        [Required]
        [Display(Name = "JobInfo:TypeOfJobName")]
        public virtual Guid TypeOfJobId { get; set; }
    }
}
