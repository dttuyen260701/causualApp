using PBL6.CasualManager.Enum;
using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.Accounts
{
    public class CustomerInfoAllDto
    {
        public string Email { get; set; }

        public string UserName { get; set; }

        public string Name { get; set; }

        public string Phone { get; set; }

        public Guid Id { get; set; }

        public Gender Gender { get; set; }

        public string Address { get; set; }
        public string AddressPoint { get; set; }

        public string DateOfBirth { get; set; }

        public string ProvinceId { get; set; }

        public string ProvinceName { get; set; }

        public string DistrictId { get; set; }

        public string DistrictName { get; set; }

        public string WardId { get; set; }

        public string WardName { get; set; }

        public string Avatar { get; set; }

        public string Role { get; set; }
    }
}
