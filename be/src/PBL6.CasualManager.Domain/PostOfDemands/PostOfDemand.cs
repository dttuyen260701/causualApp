using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.WorkerResponses;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Volo.Abp.Domain.Entities.Auditing;

namespace PBL6.CasualManager.PostOfDemands
{
    public class PostOfDemand : FullAuditedAggregateRoot<Guid>
    {
        public Guid CustomerId { get; set; }

        public Guid JobInfoId { get; set; }

        [Required]
        public string Description { get; set; }

        public string Note { get; set; }

        public bool IsActive { get; set; } = true;

        [Required]
        public DateTime EndTime { get; set; }

        public virtual CustomerInfo CustomerInfo { get; set; }

        public virtual JobInfo JobInfo { get; set; }

        public ICollection<WorkerResponse> WorkerResponses { get; set; }

        public PostOfDemand()
        {
            WorkerResponses = new HashSet<WorkerResponse>();
        }
    }
}
