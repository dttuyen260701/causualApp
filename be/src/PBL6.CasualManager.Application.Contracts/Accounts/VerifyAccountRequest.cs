using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.Accounts
{
    public class VerifyAccountRequest
    {
        public string UserName { get; set; }
        public string DateOfBirth { get; set; }
        public string Email { get; set; }
        public string PhoneNumber { get; set; }
    }
}
