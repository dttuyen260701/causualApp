using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.PrieceDetails;
using PBL6.CasualManager.WorkerInfos;
using System;
using System.ComponentModel.DataAnnotations;
using Volo.Abp.Domain.Entities.Auditing;

namespace PBL6.CasualManager.Orders
{
    public class Order : FullAuditedAggregateRoot<Guid>
    {
        public Guid JobInfoId { get; set; }

        public Guid CustomerId { get; set; }

        public Guid WorkerId { get; set; }

        public Guid PrieceDetailId { get; set; }

        public string Note { get; set; }

        [Required]
        public float JobPrices { get; set; }

        [Required]
        public string CustomerAddress { get; set; }

        [Required]
        public string CustomerAddressPoint { get; set; }

        public bool IsPaid { get; set; } = false;

        public OrderStatus Status { get; set; }

        public virtual JobInfo JobInfo { get; set; }

        public virtual CustomerInfo CustomerInfo { get; set; }

        public virtual WorkerInfo WorkerInfo { get; set; }

        public virtual PrieceDetail PrieceDetail { get; set; }
    }
}
