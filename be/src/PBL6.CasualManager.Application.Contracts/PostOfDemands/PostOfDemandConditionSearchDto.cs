using System;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.PostOfDemands
{
    public class PostOfDemandConditionSearchDto : PagedAndSortedResultRequestDto
    {
        public Guid? FilterCustomer { get; set; }
    }
}
