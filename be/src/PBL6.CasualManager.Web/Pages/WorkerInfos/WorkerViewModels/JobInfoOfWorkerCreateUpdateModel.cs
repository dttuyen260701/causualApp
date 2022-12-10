using Microsoft.AspNetCore.Mvc;
using System;
using System.ComponentModel.DataAnnotations;
using Volo.Abp.AspNetCore.Mvc.UI.Bootstrap.TagHelpers.Form;

namespace PBL6.CasualManager.Web.Pages.WorkerInfos.WorkerViewModels
{
    public class JobInfoOfWorkerCreateUpdateModel
    {
        [HiddenInput]
        public Guid Id { get; set; }

        [HiddenInput]
        public Guid WorkerId { get; set; }

        [Required]
        [Display(Name = "JobInfoOfWorker:JobInfo")]
        public virtual Guid JobInfoId { get; set; }

        [Required]
        [TextArea]
        [Display(Name = "JobInfoOfWorker:Note")]
        public string Note { get; set; }
    }
}
