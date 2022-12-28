using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using System.ComponentModel.DataAnnotations;
using System.Xml.Linq;
using System;
using Volo.Abp.AspNetCore.Mvc.UI.Bootstrap.TagHelpers.Form;
using System.Collections.Generic;
using Microsoft.AspNetCore.Mvc.Rendering;
using System.Threading.Tasks;
using PBL6.CasualManager.Plannings;
using System.Linq;

namespace PBL6.CasualManager.Web.Pages.Plannings
{
    public class IndexModel : CasualManagerPageModel
    {
        private readonly IPlanningAppService _planningAppService;

        public SearchViewModel ViewModel { get; set; }

        public List<SelectListItem> ListYear { get; set; }

        public IndexModel(IPlanningAppService planningAppService)
        {
            _planningAppService = planningAppService;
        }

        public virtual async Task OnGetAsync()
        {
            var years = await _planningAppService.GetListYearHaveData();
            if (years.Count == 0)
            {
                ListYear = new List<SelectListItem>() {
                    new SelectListItem()
                    {
                        Value = DateTime.Now.Year.ToString(),
                        Text = DateTime.Now.Year.ToString()
                    }
                };
            }
            else
            {
                ListYear = years.Select(x =>
                    new SelectListItem()
                    {
                        Value = x.ToString(),
                        Text = x.ToString()
                    }).ToList();
            }
        }

        public class SearchViewModel
        {
            [Display(Name = "Planning:Search:Year")]
            [SelectItems(nameof(ListYear))]
            public int FilterYear { get; set; }
        }
    }
}
