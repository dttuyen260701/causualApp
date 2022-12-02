using Microsoft.AspNetCore.Http;
using PBL6.CasualManager.Enum;
using System;

namespace PBL6.CasualManager.Accounts
{
    public class CustomerInfoUpdateRequest
    {
        public string Name { get; set; }

        public string Phone { get; set; }

        public Guid Id { get; set; }

        public Gender Gender { get; set; }

        public string Address { get; set; }

        public string DateOfBirth { get; set; }

        public string ProvinceId { get; set; }

        public string ProvinceName { get; set; }

        public string DistrictId { get; set; }

        public string DistrictName { get; set; }

        public string WardId { get; set; }

        public string WardName { get; set; }

        public IFormFile Avatar { get; set; }

    }
}
