using PBL6.CasualManager.Enum;
using System;

namespace PBL6.CasualManager.Accounts
{
    public class UserInfoAllDto
    {
        public Guid Id { get; set; }
        public string Email { get; set; }
        public string PhoneNumber { get; set; }
        public string Name { get; set; }
        public string IdentityCard { get; set; }
        public Gender Gender { get; set; }
        public DateTime DateOfBirth { get; set; }
        public string Address { get; set; }
        public string AddressPoint { get; set; }
        public string Workingime { get; set; }
        public string Role { get; set; }
    }
}