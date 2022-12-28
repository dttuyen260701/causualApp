using System;
using System.Collections.Generic;
using System.Text;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.Plannings
{
    public class PlanningConditionSearchDto : PagedAndSortedResultRequestDto
    {
        public int FilterYear { get; set; }
    }
}
