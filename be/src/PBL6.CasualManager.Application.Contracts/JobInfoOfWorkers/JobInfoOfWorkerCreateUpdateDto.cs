using System;

namespace PBL6.CasualManager.JobInfoOfWorkers
{
    public class JobInfoOfWorkerCreateUpdateDto
    {
        public Guid WorkerId { get; set; }

        public Guid JobInfoId { get; set; }

        public string Note { get; set; }
    }
}
