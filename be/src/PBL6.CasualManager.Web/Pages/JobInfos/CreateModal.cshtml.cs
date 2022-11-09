using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Mvc.Rendering;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.LookupValues;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.Web.Pages.JobInfos.JobInfoViewModels;
using PBL6.CasualManager.Web.Pages.TypeOfJobs.TypeOfJobViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Volo.Abp.AspNetCore.Mvc.UI.Bootstrap.TagHelpers.Form;

namespace PBL6.CasualManager.Web.Pages.JobInfos
{
    public class CreateModalModel : CasualManagerPageModel
    {
        private readonly IJobInfoAppService _jobInfoAppService;
        private readonly ITypeOfJobAppService _typeOfJobAppService;

        [BindProperty]
        public JobInfoCreateViewModel ViewModel { get; set; }

        public List<SelectListItem> ListTypeOfJobs { get; set; }

        public CreateModalModel(
            IJobInfoAppService jobInfoAppService,
            ITypeOfJobAppService typeOfJobAppService)
        {
            _jobInfoAppService = jobInfoAppService;
            _typeOfJobAppService = typeOfJobAppService;
        }

        public virtual async Task OnGetAsync()
        {
            var typeOfJobs = await _typeOfJobAppService.GetLookupValuesAsync();
            ListTypeOfJobs = ObjectMapper.Map<List<LookupValueDto>, List<SelectListItem>>(typeOfJobs);
            ViewModel = new JobInfoCreateViewModel();
        }

        public virtual async Task<ActionResult> OnPostAsync()
        {
            var createInfo = ObjectMapper.Map<JobInfoCreateViewModel, JobInfoCreateUpdateDto>(ViewModel);
            await _jobInfoAppService.CreateAsync(createInfo);
            return NoContent();
        }

        public class JobInfoCreateViewModel : JobInfoCreateUpdateViewModel
        {
            [SelectItems(nameof(ListTypeOfJobs))]
            public override Guid TypeOfJobId { get; set; }
        }
    }
}
