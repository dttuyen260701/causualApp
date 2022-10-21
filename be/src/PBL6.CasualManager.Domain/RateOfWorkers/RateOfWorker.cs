using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.WorkerInfos;
using System;
using System.ComponentModel.DataAnnotations;
using Volo.Abp.Domain.Entities.Auditing;

namespace PBL6.CasualManager.RateOfWorkers
{
    public class RateOfWorker : FullAuditedAggregateRoot<Guid>
    {
        public Guid WorkerId { get; set; }

        public Guid CustomerId { get; set; }

        [Required]
        public int AttitudeRate { get; set; }

        [Required]
        public int SkillRate { get; set; }

        [Required]
        public int PleasureRate { get; set; }

        public string Comment { get; set; }

        public virtual WorkerInfo WorkerInfo { get; set; }

        public virtual CustomerInfo CustomerInfo { get; set; }
    }
}
