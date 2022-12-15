using System;
using System.Collections.Generic;
using System.Text;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.Oders
{
    public class OrderConditionSearchDto : PagedAndSortedResultRequestDto
    {
        public Guid? WorkerId { get; set; }

        public Guid? CustomerId { get; set; }
    }
}
