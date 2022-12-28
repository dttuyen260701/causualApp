using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.RateOfWorkers;
using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.PostOfDemands
{
    public class WorkerRequestInPostOfDemandResponse
    {
        public Guid Id { get; set; }
        public string Name { get; set; }
        public string LinkIMG { get; set; }
        public List<JobInfoResponse> ListJobInfo { get; set; }
        public RateOfWorkerResponse RateDetail { get; set; }
        public int TotalReviews { get; set; }
    }
}
