using PBL6.CasualManager.Enum;
using System;
using System.ComponentModel.DataAnnotations;

namespace PBL6.CasualManager.WorkerInfos
{
    public class WorkerInfoCreateUpdateDto
    {
        public Guid Id { get; set; }

        public string Name { get; set; }

        public string UserName { get; set; }

        public string Password { get; set; }

        public string Phone { get; set; }

        public string Email { get; set; }

        public Gender Gender { get; set; }

        public DateTime DateOfBirth { get; set; }

        public string Address { get; set; }

        public string ProvinceId { get; set; }

        public string ProvinceName { get; set; }

        public string DistrictId { get; set; }

        public string DistrictName { get; set; }

        public string WardId { get; set; }

        public string WardName { get; set; }

        public string Avatar { get; set; }

        public string IdentityCard { get; set; }

        public DateTime IdentityCardDate { get; set; }

        public string IdentityCardBy { get; set; }

        public string StartWorkingTime { get; set; }

        public string EndWorkingTime { get; set; }

        public int AverageRate { get; set; } = 0;

        public WorkerStatus Status { get; set; } = WorkerStatus.Free;

        public bool IsActive { get; set; } = true;
    }
}
