using System.Threading.Tasks;
using System;

namespace PBL6.CasualManager.Web.Pages.WorkerInfos
{
    public class HitoryOfWorkerModel : CasualManagerPageModel
    {
        public SearchModel ViewModel { get; set; }

        public virtual async Task OnGetAsync(Guid id)
        {
            ViewModel = new SearchModel { WorkerId = id };
        }

        public class SearchModel
        {
            public Guid WorkerId { get; set; }
        }
    }
}
