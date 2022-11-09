using System;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.TypeOfJobs
{
    public class TypeOfJobDto : FullAuditedEntityDto<Guid>
    {
        public string Name { get; set; }

        public string Description { get; set; }
    }
}
