using PBL6.CasualManager.Districts;
using System;
using System.ComponentModel.DataAnnotations;
using Volo.Abp.Domain.Entities;

namespace PBL6.CasualManager.Wards
{
    public class Ward : Entity<Guid>
    {
        [Required]
        [MaxLength(512)]
        public string Name { get; set; }

        public Guid DistrictId { get; set; }

        public virtual District District { get; set; }
    }
}
