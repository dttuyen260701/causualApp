using PBL6.CasualManager.Districts;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using Volo.Abp.Domain.Entities;

namespace PBL6.CasualManager.Provinces
{
    public class Province : Entity<Guid>
    {
        [Required]
        [MaxLength(512)]
        public string Name { get; set; }

        public ICollection<District> Districts { get; set; }

        public Province()
        {
            Districts = new HashSet<District>();
        }
    }
}
