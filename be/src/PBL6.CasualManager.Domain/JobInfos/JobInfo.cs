using PBL6.CasualManager.JobInfoOfWorkers;
using PBL6.CasualManager.Orders;
using PBL6.CasualManager.PostOfDemands;
using PBL6.CasualManager.TypeOfJobs;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Volo.Abp.Domain.Entities.Auditing;

namespace PBL6.CasualManager.JobInfos
{
    public class JobInfo : FullAuditedAggregateRoot<Guid>
    {
        [Required]
        public string Name { get; set; }

        [Required]
        public string Description { get; set; }

        [Required]
        public float Prices { get; set; }

        public Guid TypeOfJobId { get; set; }

        public virtual TypeOfJob TypeOfJob { get; set; }

        public ICollection<JobInfoOfWorker> JobInfoOfWorkers { get; set; }

        public ICollection<PostOfDemand> PostOfDemands { get; set; }

        public ICollection<Order> Orders { get; set; }

        public JobInfo()
        {
            JobInfoOfWorkers = new HashSet<JobInfoOfWorker>();
            PostOfDemands = new HashSet<PostOfDemand>();
            Orders = new HashSet<Order>();
        }
    }
}
