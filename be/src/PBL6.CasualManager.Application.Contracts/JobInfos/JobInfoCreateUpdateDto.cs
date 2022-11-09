using System;
using System.ComponentModel.DataAnnotations;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.JobInfos
{
    public class JobInfoCreateUpdateDto
    {
        public Guid Id { get; set; }

        [Required]
        public string Name { get; set; }

        [Required]
        public string Description { get; set; }

        [Required]
        public float Prices { get; set; }

        public Guid TypeOfJobId { get; set; }
    }
}
