using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.Accounts
{
    public class ChangePasswordWithoutOldPasswordRequest
    {
        public string UserName { get; set; }
        public string NewPassword { get; set; }
    }
}
