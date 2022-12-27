using System;
using Volo.Abp.Domain.Entities.Auditing;

namespace PBL6.CasualManager.Plannings
{
    public class Planning : FullAuditedAggregateRoot<Guid>
    {
        public int RevenueTarget { get; set; }

        public int Month { get; set; }

        public int Year { get; set; }
    }
}
