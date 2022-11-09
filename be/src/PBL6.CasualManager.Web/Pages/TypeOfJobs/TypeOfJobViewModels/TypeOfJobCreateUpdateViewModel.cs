using Microsoft.AspNetCore.Mvc;
using System;
using System.ComponentModel.DataAnnotations;

namespace PBL6.CasualManager.Web.Pages.TypeOfJobs.TypeOfJobViewModels
{
    public class TypeOfJobCreateUpdateViewModel
    {
        [HiddenInput]
        public Guid Id { get; set; }

        [Required]
        [Display(Name = "TypeOfJob:Name", Prompt = "TypeOfJob:EnterName")]
        public string Name { get; set; }

        [Required]
        [Display(Name = "TypeOfJob:Description", Prompt = "TypeOfJob:EnterDescription")]
        public string Description { get; set; }
    }
}
