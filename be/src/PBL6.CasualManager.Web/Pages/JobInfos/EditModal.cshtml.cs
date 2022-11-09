using AutoMapper.Internal.Mappers;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Mvc.Rendering;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.LookupValues;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.Web.Pages.JobInfos.JobInfoViewModels;
using System.Collections.Generic;
using System.Threading.Tasks;
using System;
using Volo.Abp.AspNetCore.Mvc.UI.Bootstrap.TagHelpers.Form;
using Volo.Abp.ObjectMapping;
using System.Linq;

namespace PBL6.CasualManager.Web.Pages.JobInfos
{
    public class EditModalModel : CasualManagerPageModel
    {
        private readonly IJobInfoAppService _jobInfoAppService;
        private readonly ITypeOfJobAppService _typeOfJobAppService;

        [BindProperty]
        public JobInfoUpdateViewModel ViewModel { get; set; }

        public List<SelectListItem> ListTypeOfJobs { get; set; }

        public EditModalModel(
            IJobInfoAppService jobInfoAppService,
            ITypeOfJobAppService typeOfJobAppService)
        {
            _jobInfoAppService = jobInfoAppService;
            _typeOfJobAppService = typeOfJobAppService;
        }

        public virtual async Task OnGetAsync(Guid id)
        {
            var jobInfo = await _jobInfoAppService.GetAsync(id);
            ViewModel = ObjectMapper.Map<JobInfoDto, JobInfoUpdateViewModel>(jobInfo);

            var typeOfJobs = await _typeOfJobAppService.GetLookupValuesAsync();
            ListTypeOfJobs = typeOfJobs.Select(i =>
                new SelectListItem()
                {
                    Selected = i.Id == jobInfo.TypeOfJobId,
                    Value = i.Id.ToString(),
                    Text = i.Name
                }).ToList();
        }

        public virtual async Task<ActionResult> OnPostAsync()
        {
            var updateInfo = ObjectMapper.Map<JobInfoUpdateViewModel, JobInfoCreateUpdateDto>(ViewModel);
            await _jobInfoAppService.UpdateAsync(ViewModel.Id, updateInfo);
            return NoContent();
        }

        public class JobInfoUpdateViewModel : JobInfoCreateUpdateViewModel
        {
            [SelectItems(nameof(ListTypeOfJobs))]
            public override Guid TypeOfJobId { get; set; }
        }
    }
}
