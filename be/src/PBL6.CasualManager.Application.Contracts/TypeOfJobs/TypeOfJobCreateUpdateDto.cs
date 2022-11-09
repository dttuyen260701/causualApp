using System;
using System.ComponentModel.DataAnnotations;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.TypeOfJobs
{
    public class TypeOfJobCreateUpdateDto : FullAuditedEntityDto<Guid>
    {
        [Required]
        public string Name { get; set; }

        public string Description { get; set; }
    }
}
