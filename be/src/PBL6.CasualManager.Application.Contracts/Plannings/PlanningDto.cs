using System;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.Plannings
{
    public class PlanningDto : FullAuditedEntityDto<Guid>
    {
        public int RevenueTarget { get; set; }

        public int Month { get; set; }

        public int Year { get; set; }
    }
}
