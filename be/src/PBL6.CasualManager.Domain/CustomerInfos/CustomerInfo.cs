using PBL6.CasualManager.Enum;
using PBL6.CasualManager.Orders;
using PBL6.CasualManager.PostOfDemands;
using PBL6.CasualManager.RateOfWorkers;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Volo.Abp.Domain.Entities.Auditing;

namespace PBL6.CasualManager.CustomerInfos
{
    public class CustomerInfo : FullAuditedAggregateRoot<Guid>
    {
        public Guid UserId { get; set; }

        public Gender Gender { get; set; }

        [Required]
        public string Address { get; set; }

        [Required]
        public string AddressPoint { get; set; }

        public ICollection<RateOfWorker> RateOfWorkers { get; set; }

        public ICollection<Order> Orders { get; set; }

        public ICollection<PostOfDemand> PostOfDemands { get; set; }

        public CustomerInfo()
        {
            RateOfWorkers = new HashSet<RateOfWorker>();
            PostOfDemands = new HashSet<PostOfDemand>();
            Orders = new HashSet<Order>();
        }
    }
}
