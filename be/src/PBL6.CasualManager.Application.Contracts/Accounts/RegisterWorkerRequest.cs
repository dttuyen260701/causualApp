using Microsoft.AspNetCore.Http;
using PBL6.CasualManager.Enum;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace PBL6.CasualManager.Accounts
{
    public class RegisterWorkerRequest
    {
        public string Email { get; set; }

        public string UserName { get; set; }

        public string Password { get; set; }

        public string Name { get; set; }

        public string Phone { get; set; }

        public string IdentityCard { get; set; }

        public string IdentityCardDate { get; set; }

        public string IdentityCardBy { get; set; }

        public Gender Gender { get; set; }

        public string DateOfBirth { get; set; }

        public string StartWorkingTime { get; set; }

        public string EndWorkingTime { get; set; }

        public string ProvinceId { get; set; }

        public string ProvinceName { get; set; }

        public string DistrictId { get; set; }

        public string DistrictName { get; set; }

        public string WardId { get; set; }

        public string WardName { get; set; }

        public string Address { get; set; } 

    }
}
