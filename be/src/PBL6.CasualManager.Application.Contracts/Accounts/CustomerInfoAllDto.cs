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

        public Guid UserId { get; set; }

        public Gender Gender { get; set; }

        public string Address { get; set; }

        public string AddressPoint { get; set; }
    }
}
