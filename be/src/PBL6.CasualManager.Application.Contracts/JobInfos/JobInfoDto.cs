using System;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.JobInfos
{
    public class JobInfoDto : FullAuditedEntityDto<Guid>
    {
        public string Name { get; set; }

        public string Description { get; set; }

        public float Prices { get; set; }

        public Guid TypeOfJobId { get; set; }

        public string TypeOfJobName { get; set; }

        public string TypeOfJobIcon { get; set; }
    }
}
