using System;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.JobInfoOfWorkers
{
    public class JobInfoOfWorkerDto : FullAuditedEntityDto<Guid>
    {
        public Guid TypeOfJobId { get; set; }

        public Guid JobInfoId { get; set; }

        public Guid WorkerId { get; set; }

        public string Note { get; set; }

        public string JobName { get; set; }

        public string TypeOfJobName { get; set; }

        public int Prices { get; set; }
    }
}
