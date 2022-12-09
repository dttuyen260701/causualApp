using PBL6.CasualManager.Enum;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.RateOfWorkers;
using System;
using System.Collections.Generic;
using System.Text;
using static PBL6.CasualManager.Permissions.CasualManagerPermissions;

namespace PBL6.CasualManager.WorkerInfos
{
    public class WorkerInfoResponse
    {
        public Guid Id { get; set; }
        public string Name { get; set; }
        public string LinkIMG { get; set; }
        public RateOfWorkerResponse RateDetail { get; set; }
        public int TotalReviews { get; set; }
        public string Address { get; set; }
        public string Phone { get; set; }
        public string WorkingTime { get; set; }
        public string ProvinceId { get; set; }
        public string DistrictId { get; set; }
        public string WardId { get; set; }
        public WorkerStatus WorkerStatus { get; set; }
        public List<JobInfoResponse> ListJobInfo { get; set; }
    }
}
