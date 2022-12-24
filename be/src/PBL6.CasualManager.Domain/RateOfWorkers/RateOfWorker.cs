using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Orders;
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

        public Guid OrderId { get; set; }

        [Required]
        [Range(1, 5)]
        public int AttitudeRate { get; set; }

        [Required]
        [Range(1, 5)]
        public int SkillRate { get; set; }

        [Required]
        [Range(1, 5)]
        public int PleasureRate { get; set; }

        public string Comment { get; set; }

        public virtual WorkerInfo WorkerInfo { get; set; }

        public virtual CustomerInfo CustomerInfo { get; set; }
    }
}
