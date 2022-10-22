using PBL6.CasualManager.Orders;
using System;
using System.ComponentModel.DataAnnotations;
using Volo.Abp.Domain.Entities.Auditing;

namespace PBL6.CasualManager.PrieceDetails
{
    public class PrieceDetail : FullAuditedAggregateRoot<Guid>
    {
        [Required]
        public float FeeForWorker { get; set; }

        [Required]
        public float FeeForBussiness { get; set; }
        
        public virtual Order Order { get; set; }
    }
}
