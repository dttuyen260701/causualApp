using PBL6.CasualManager.Enum;
using PBL6.CasualManager.JobInfoOfWorkers;
using PBL6.CasualManager.Orders;
using PBL6.CasualManager.RateOfWorkers;
using PBL6.CasualManager.WorkerResponses;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Volo.Abp.Domain.Entities.Auditing;

namespace PBL6.CasualManager.WorkerInfos
{
    public class WorkerInfo : FullAuditedAggregateRoot<Guid>
    {
        public Guid UserId { get; set; }

        [Required]
        [MaxLength(12)]
        public string IdentityCard { get; set; }

        [Required]
        public DateTime IdentityCardDate { get; set; }

        [Required]
        [MaxLength(256)]
        public string IdentityCardBy { get; set; }

        [Required]
        public Gender Gender { get; set; }

        [Required]
        public DateTime DateOfBirth { get; set; }

        [Required]
        public string StartWorkingTime { get; set; }

        [Required]
        public string EndWorkingTime { get; set; }

        public string ProvinceId { get; set; }

        public string ProvinceName { get; set; }

        public string DistrictId { get; set; }

        public string DistrictName { get; set; }

        public string WardId { get; set; }

        public string WardName { get; set; }

        public string Address { get; set; }

        public string AddressPoint { get; set; }

        public int AverageRate { get; set; }

        public WorkerStatus Status { get; set; }

        public bool IsActive { get; set; }

        public string Avatar { get; set; }

        public ICollection<JobInfoOfWorker> JobInfoOfWorkers { get; set; }

        public ICollection<RateOfWorker> RateOfWorkers { get; set; }

        public ICollection<Order> Orders { get; set; }

        public ICollection<WorkerResponse> WorkerResponses { get; set; }

        public WorkerInfo()
        {
            JobInfoOfWorkers = new HashSet<JobInfoOfWorker>();
            RateOfWorkers = new HashSet<RateOfWorker>();
            WorkerResponses = new HashSet<WorkerResponse>();
            Orders = new HashSet<Order>();
        }
    }
}
