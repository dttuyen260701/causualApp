using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.PostOfDemands
{
    public class WorkerResponseRequest
    {
        public Guid WorkerId { get; set; }
        public Guid PostOfDemandId { get; set; }
    }
}
