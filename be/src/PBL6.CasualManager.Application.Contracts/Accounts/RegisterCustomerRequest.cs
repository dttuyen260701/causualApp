using PBL6.CasualManager.Enum;
using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.Accounts
{
    //purpose: to contain info for registering customer
    public class RegisterCustomerRequest
    {
        public string Email { get; set; }
        public string UserName { get; set; }
        public string Password { get; set; }
        public string Name { get; set; }
        public string Phone { get; set; }
        public Gender Gender { get; set; }
        public string Address { get; set; }
    }
}
