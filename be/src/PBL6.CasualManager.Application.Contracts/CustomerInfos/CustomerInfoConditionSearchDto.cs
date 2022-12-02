using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.CustomerInfos
{
    public class CustomerInfoConditionSearchDto : PagedAndSortedResultRequestDto
    {
        // Search by name or username
        public string Keyword { get; set; }
    }
}
