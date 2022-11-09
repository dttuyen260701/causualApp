using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Mvc.Rendering;
using PBL6.CasualManager.LookupValues;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.Web.Pages.TypeOfJobs.TypeOfJobViewModels;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;
using System.Xml.Linq;
using Volo.Abp.Application.Dtos;
using Volo.Abp.ObjectMapping;

namespace PBL6.CasualManager.Web.Pages.JobInfos
{
    public class IndexModel : CasualManagerPageModel
    {
        private readonly ITypeOfJobAppService _typeOfJobAppService;

        [BindProperty]
        public SearchViewModel ViewModel { get; set; }

        public List<SelectListItem> ListTypeOfJobs { get; set; }

        public IndexModel(ITypeOfJobAppService typeOfJobAppService)
        {
            _typeOfJobAppService = typeOfJobAppService;
        }

        public virtual async Task OnGetAsync()
        {
            var typeOfJobs = await _typeOfJobAppService.GetLookupValuesAsync();
            ListTypeOfJobs = new List<SelectListItem> { new SelectListItem() { Value = "", Text = L["Common:All"] } };
            ListTypeOfJobs.AddRange(ObjectMapper.Map<List<LookupValueDto>, List<SelectListItem>>(typeOfJobs));
        }

        public class SearchViewModel
        {
            [Display(Name = "JobInfo:Key", Prompt = "JobInfo:EnterNameOrTypeOfJobName")]
            public string FilterName { get; set; }

            [Display(Name = "JobInfo:FilterTypeOfJob")]
            public Guid FilterTypeOfJob { get; set; }
        }
    }
}
