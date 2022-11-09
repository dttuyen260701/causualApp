using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.TypeOfJobs
{
    public class TypeOfJobConditionSearchDto : PagedAndSortedResultRequestDto
    {
        public string FilterName { get; set; }
    }
}
