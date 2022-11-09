using PBL6.CasualManager.JobInfos;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Volo.Abp.Domain.Entities.Auditing;

namespace PBL6.CasualManager.TypeOfJobs
{
    public class TypeOfJob : FullAuditedAggregateRoot<Guid>
    {
        [Required]
        [MaxLength(512)]
        public string Name { get; set; }

        public string Description { get; set; }

        public string Avatar { get; set; }

        public ICollection<JobInfo> JobInfos { get; set; }

        public TypeOfJob()
        {
            JobInfos = new HashSet<JobInfo>();
        }
    }
}
