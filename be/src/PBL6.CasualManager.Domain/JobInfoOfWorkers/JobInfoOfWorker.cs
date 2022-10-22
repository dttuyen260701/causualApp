using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.WorkerInfos;
using System;
using Volo.Abp.Domain.Entities.Auditing;

namespace PBL6.CasualManager.JobInfoOfWorkers
{
    public class JobInfoOfWorker : FullAuditedAggregateRoot<Guid>
    {
        public Guid JobInfoId { get; set; }

        public Guid WorkerId { get; set; }

        public string Note { get; set; }

        public virtual JobInfo JobInfo { get; set; }

        public virtual WorkerInfo WorkerInfo { get; set; }
    }
}
