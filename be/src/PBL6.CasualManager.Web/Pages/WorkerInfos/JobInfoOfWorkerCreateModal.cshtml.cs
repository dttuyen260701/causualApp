using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using PBL6.CasualManager.JobInfoOfWorkers;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.LookupValues;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.Web.Pages.WorkerInfos.WorkerViewModels;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using Volo.Abp.AspNetCore.Mvc.UI.Bootstrap.TagHelpers.Form;

namespace PBL6.CasualManager.Web.Pages.WorkerInfos
{
    public class JobInfoOfWorkerCreateModalModel : CasualManagerPageModel
    {
        private readonly IJobInfoAppService _jobInfoAppService;
        private readonly ITypeOfJobAppService _typeOfJobAppService;
        private readonly IJobInfoOfWorkerAppService _jobInfoOfWorkerAppService;

        [BindProperty]
        public JobInfoOfWorkerCreateViewModel ViewModel { get; set; }

        public List<SelectListItem> ListTypeOfJob { get; set; }

        public List<SelectListItem> ListJobInfo { get; set; }

        public JobInfoOfWorkerCreateModalModel(
            IJobInfoAppService jobInfoAppService,
            ITypeOfJobAppService typeOfJobAppService,
            IJobInfoOfWorkerAppService jobInfoOfWorkerAppService)
        {
            _jobInfoAppService = jobInfoAppService;
            _typeOfJobAppService = typeOfJobAppService;
            _jobInfoOfWorkerAppService = jobInfoOfWorkerAppService;
        }
        public virtual async Task OnGetAsync(Guid workerId)
        {
            ViewModel = new JobInfoOfWorkerCreateViewModel() { WorkerId = workerId };

            var typeOfJobs = await _typeOfJobAppService.GetLookupValuesAsync();
            ListTypeOfJob = ObjectMapper.Map<List<LookupValueDto>, List<SelectListItem>>(typeOfJobs);

            var jobInfos = await _jobInfoAppService.GetListByTypeOfJobAsync(typeOfJobs.First().Id);
            ListJobInfo = jobInfos.Select(x =>
                new SelectListItem()
                {
                    Value = x.Id.ToString(),
                    Text = $"{x.Name} - {String.Format(CultureInfo.GetCultureInfo("vi-VN"), "{0:c}", x.Prices)}"
                }).ToList();
        }

        public virtual async Task<ActionResult> OnPostAsync()
        {
            var jobInfoOfWorkerCreateUpdateDto = ObjectMapper.Map<JobInfoOfWorkerCreateViewModel, JobInfoOfWorkerCreateUpdateDto>(ViewModel);
            await _jobInfoOfWorkerAppService.CreateJobInfoOfWorker(jobInfoOfWorkerCreateUpdateDto);
            return NoContent();
        }

        public class JobInfoOfWorkerCreateViewModel : JobInfoOfWorkerCreateUpdateModel
        {
            [SelectItems(nameof(ListTypeOfJob))]
            [Display(Name = "JobInfoOfWorker:TypeOfJob")]
            public Guid TypeOfJobId { get; set; }

            [SelectItems(nameof(ListJobInfo))]
            public override Guid JobInfoId { get; set; }
        }
    }
}
