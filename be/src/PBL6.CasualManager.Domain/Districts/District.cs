using PBL6.CasualManager.Wards;
using PBL6.CasualManager.Provinces;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Volo.Abp.Domain.Entities;

namespace PBL6.CasualManager.Districts
{
    public class District : Entity<Guid>
    {
        [Required]
        [MaxLength(512)]
        public string Name { get; set; }

        public Guid ProvinceId { get; set; }

        public virtual Province Province { get; set; }

        public ICollection<Ward> Wards { get; set; }

        public District()
        {
            Wards = new HashSet<Ward>();
        }
    }
}
