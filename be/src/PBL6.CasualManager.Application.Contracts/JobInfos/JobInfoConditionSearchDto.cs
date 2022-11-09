using System;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.JobInfos
{
    public class JobInfoConditionSearchDto : PagedAndSortedResultRequestDto
    {
        public string FilterName { get; set; }

        public Guid? FilterTypeOfJob { get; set; }
    }
}
