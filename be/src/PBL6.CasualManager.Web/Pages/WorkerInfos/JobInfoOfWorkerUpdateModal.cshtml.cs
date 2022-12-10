using Microsoft.AspNetCore.Mvc;
using PBL6.CasualManager.Web.Pages.WorkerInfos.WorkerViewModels;
using System.ComponentModel.DataAnnotations;
using System;
using Volo.Abp.AspNetCore.Mvc.UI.Bootstrap.TagHelpers.Form;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc.Rendering;
using PBL6.CasualManager.LookupValues;
using System.Collections.Generic;
using PBL6.CasualManager.JobInfoOfWorkers;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.TypeOfJobs;
using System.Linq;
using System.Globalization;
using static PBL6.CasualManager.Web.Pages.WorkerInfos.JobInfoOfWorkerCreateModalModel;
using Volo.Abp.ObjectMapping;

namespace PBL6.CasualManager.Web.Pages.WorkerInfos
{
    public class JobInfoOfWorkerUpdateModalModel : CasualManagerPageModel
    {
        private readonly IJobInfoAppService _jobInfoAppService;
        private readonly ITypeOfJobAppService _typeOfJobAppService;
        private readonly IJobInfoOfWorkerAppService _jobInfoOfWorkerAppService;

        [BindProperty]
        public JobInfoOfWorkerUpdateViewModel ViewModel { get; set; }

        public List<SelectListItem> ListTypeOfJob { get; set; }

        public List<SelectListItem> ListJobInfo { get; set; }

        public JobInfoOfWorkerUpdateModalModel(
            IJobInfoAppService jobInfoAppService,
            ITypeOfJobAppService typeOfJobAppService,
            IJobInfoOfWorkerAppService jobInfoOfWorkerAppService)
        {
            _jobInfoAppService = jobInfoAppService;
            _typeOfJobAppService = typeOfJobAppService;
            _jobInfoOfWorkerAppService = jobInfoOfWorkerAppService;
        }

        public virtual async Task OnGetAsync(Guid jobInfoOfWorkerId)
        {
            var jobInfoOfWorker = await _jobInfoOfWorkerAppService.GetAsync(jobInfoOfWorkerId);
            ViewModel = ObjectMapper.Map<JobInfoOfWorkerDto, JobInfoOfWorkerUpdateViewModel>(jobInfoOfWorker);

            var typeOfJobs = await _typeOfJobAppService.GetLookupValuesAsync();
            ListTypeOfJob = ObjectMapper.Map<List<LookupValueDto>, List<SelectListItem>>(typeOfJobs);
            ViewModel.TypeOfJobId = jobInfoOfWorker.TypeOfJobId;

            var jobInfos = await _jobInfoAppService.GetListByTypeOfJobAsync(jobInfoOfWorker.TypeOfJobId);
            ListJobInfo = jobInfos.Select(x =>
                new SelectListItem()
                {
                    Value = x.Id.ToString(),
                    Text = $"{x.Name} - {String.Format(CultureInfo.GetCultureInfo("vi-VN"), "{0:c}", x.Prices)}"
                }).ToList();
            ViewModel.JobInfoId = jobInfoOfWorker.JobInfoId;
        }

        public virtual async Task<ActionResult> OnPostAsync()
        {
            var jobInfoOfWorkerCreateUpdateDto = ObjectMapper.Map<JobInfoOfWorkerUpdateViewModel, JobInfoOfWorkerCreateUpdateDto>(ViewModel);
            await _jobInfoOfWorkerAppService.UpdateJobInfoOfWorker(ViewModel.Id, jobInfoOfWorkerCreateUpdateDto);
            return NoContent();
        }

        public class JobInfoOfWorkerUpdateViewModel : JobInfoOfWorkerCreateUpdateModel
        {
            [SelectItems(nameof(ListTypeOfJob))]
            [Display(Name = "JobInfoOfWorker:TypeOfJob")]
            public Guid TypeOfJobId { get; set; }

            [SelectItems(nameof(ListJobInfo))]
            public override Guid JobInfoId { get; set; }
        }
    }
}
