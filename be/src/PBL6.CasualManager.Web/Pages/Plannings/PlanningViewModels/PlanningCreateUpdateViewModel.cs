using Microsoft.AspNetCore.Mvc;
using System.ComponentModel.DataAnnotations;
using System;

namespace PBL6.CasualManager.Web.Pages.Plannings.PlanningViewModels
{
    public class PlanningCreateUpdateViewModel
    {
        [Display(Name = "Common:Year")]
        [Required]
        public virtual int Year { get; set; }

        [Display(Name = "Planning:TargetInJan")]
        [Range(0, int.MaxValue)]
        public int TargetInJan { get; set; } = 0;

        [Display(Name = "Planning:TargetInFeb")]
        [Range(0, int.MaxValue)]
        public int TargetInFeb { get; set; } = 0;

        [Display(Name = "Planning:TargetInMarch")]
        [Range(0, int.MaxValue)]
        public int TargetInMarch { get; set; } = 0;

        [Display(Name = "Planning:TargetInApril")]
        [Range(0, int.MaxValue)]
        public int TargetInApril { get; set; } = 0;

        [Display(Name = "Planning:TargetInMay")]
        [Range(0, int.MaxValue)]
        public int TargetInMay { get; set; } = 0;

        [Display(Name = "Planning:TargetInJune")]
        [Range(0, int.MaxValue)]
        public int TargetInJune { get; set; } = 0;

        [Display(Name = "Planning:TargetInJully")]
        [Range(0, int.MaxValue)]
        public int TargetInJully { get; set; } = 0;

        [Display(Name = "Planning:TargetInAugust")]
        [Range(0, int.MaxValue)]
        public int TargetInAugust { get; set; } = 0;

        [Display(Name = "Planning:TargetInSep")]
        [Range(0, int.MaxValue)]
        public int TargetInSep { get; set; } = 0;

        [Display(Name = "Planning:TargetInOct")]
        [Range(0, int.MaxValue)]
        public int TargetInOct { get; set; } = 0;

        [Display(Name = "Planning:TargetInNov")]
        [Range(0, int.MaxValue)]
        public int TargetInNov { get; set; } = 0;

        [Display(Name = "Planning:TargetInDec")]
        [Range(0, int.MaxValue)]
        public int TargetInDec { get; set; } = 0;
    }
}
