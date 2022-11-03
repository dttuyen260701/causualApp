using PBL6.CasualManager.Enum;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace PBL6.CasualManager.Accounts
{
    public class WorkerInfoAllDto
    {
        public string Email { get; set; }

        public string Username { get; set; }

        public string Name { get; set; }

        public string Phone { get; set; }

        public Guid UserId { get; set; }

        public string IdentityCard { get; set; }

        public Gender Gender { get; set; }

        public DateTime DateOfBirth { get; set; }

        public string Address { get; set; }

        public string AddressPoint { get; set; }

        public string Workingime { get; set; }

        public int AverageRate { get; set; }
    }
}
