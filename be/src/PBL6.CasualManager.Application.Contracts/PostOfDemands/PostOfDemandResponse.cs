using PBL6.CasualManager.JobInfos;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Text;

namespace PBL6.CasualManager.PostOfDemands
{
    public class PostOfDemandResponse
    {
        public Guid Id { get; set; }
        public Guid CustomerId { get; set; }
        public string CustomerName { get; set; }
        public DateTime EndDateTime { get; set; }
        public string EndDateTimeString { get; set; }
        public string CreationTime { get; set; }
        public string Description { get; set; }
        public string Note { get; set; }
        public Guid JobInfoId { get; set; }
        public string JobInfoName { get; set; }
        public string ImageUser { get; set; }
        public string Address { get; set; }
        public string AddressPoint { get; set; }
        public List<WorkerRequestInPostOfDemandResponse> ListWorkerRequestInPostOfDemandResponse { get; set; }
    }
}
