using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.JobInfoOfWorkers
{
    public class RegisterJobRequest
    {
        public Guid JobInfoId { get; set; }
        public string Note { get; set; }
    }
}
