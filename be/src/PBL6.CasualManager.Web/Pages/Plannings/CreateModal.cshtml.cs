using AutoMapper.Internal.Mappers;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Mvc.Rendering;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.Plannings;
using PBL6.CasualManager.Web.Pages.Plannings.PlanningViewModels;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Volo.Abp.AspNetCore.Mvc.UI.Bootstrap.TagHelpers.Form;

namespace PBL6.CasualManager.Web.Pages.Plannings
{
    public class CreateModalModel : CasualManagerPageModel
    {
        private readonly IPlanningAppService _planningAppService;

        public List<SelectListItem> ListYear { get; set; }

        [BindProperty]
        public CreateUpdateViewModel ViewModel { get; set; }

        public CreateModalModel(IPlanningAppService planningAppService)
        {
            _planningAppService = planningAppService;
        }

        public virtual async Task OnGetAsync()
        {
            var yearNow = DateTime.Now.Year;
            ListYear = new List<SelectListItem>()
            {
                new SelectListItem(){ Text = (yearNow - 2).ToString(), Value = (yearNow - 2).ToString() },
                new SelectListItem(){ Text = (yearNow - 1).ToString(), Value = (yearNow - 1).ToString() },
                new SelectListItem(){ Text = (yearNow).ToString(), Value = (yearNow).ToString() },
                new SelectListItem(){ Text = (yearNow + 1).ToString(), Value = (yearNow + 1).ToString() },
                new SelectListItem(){ Text = (yearNow + 2).ToString(), Value = (yearNow + 2).ToString() }
            };
        }

        public virtual async Task<ActionResult> OnPostAsync()
        {
            var createInfo = ObjectMapper.Map<PlanningCreateUpdateViewModel, PlanningCreateUpdateDto>(ViewModel);
            await _planningAppService.CreateUpdateRevenueTargetInYear(createInfo);
            return NoContent();
        }

        public class CreateUpdateViewModel : PlanningCreateUpdateViewModel
        {
            [SelectItems(nameof(ListYear))]
            public override int Year { get; set; }
        }
    }
}
