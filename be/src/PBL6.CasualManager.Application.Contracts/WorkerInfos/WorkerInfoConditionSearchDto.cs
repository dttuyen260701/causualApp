using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.WorkerInfos
{
    public class WorkerInfoConditionSearchDto : PagedAndSortedResultRequestDto
    {
        // Search by name or username
        public string Keyword { get; set; }
    }
}
