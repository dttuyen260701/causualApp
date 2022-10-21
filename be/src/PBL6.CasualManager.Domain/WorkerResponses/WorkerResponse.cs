using PBL6.CasualManager.PostOfDemands;
using PBL6.CasualManager.WorkerInfos;
using System;
using Volo.Abp.Domain.Entities.Auditing;

namespace PBL6.CasualManager.WorkerResponses
{
    public class WorkerResponse : FullAuditedAggregateRoot<Guid>
    {
        public Guid WorkerId { get; set; }

        public Guid PostOfDemandId { get; set; }

        public virtual WorkerInfo WorkerInfo { get; set; }

        public virtual PostOfDemand PostOfDemand { get; set; }
    }
}
