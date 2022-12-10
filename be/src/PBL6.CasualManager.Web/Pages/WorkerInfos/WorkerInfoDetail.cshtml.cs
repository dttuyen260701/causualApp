using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using System.Threading.Tasks;
using System;
using PBL6.CasualManager.WorkerInfos;
using PBL6.CasualManager.Web.Pages.WorkerInfos.WorkerViewModels;

namespace PBL6.CasualManager.Web.Pages.WorkerInfos
{
    public class WorkerInfoDetailModel : CasualManagerPageModel
    {
        private readonly IWorkerInfoAppService _workerInfoAppService;

        [BindProperty]
        public WorkerInfoDetailViewModel ViewModel { get; set; }

        public Guid WorkerId { get; set; }

        public WorkerInfoDetailModel(IWorkerInfoAppService workerInfoAppService)
        {
            _workerInfoAppService = workerInfoAppService;
        }
        public virtual async Task OnGetAsync(Guid id)
        {
            WorkerId = id;
            var workerInfo = await _workerInfoAppService.GetWorkerInfoAsync(id);
            ViewModel = ObjectMapper.Map<WorkerInfoDto, WorkerInfoDetailViewModel>(workerInfo);
        }
    }
}
