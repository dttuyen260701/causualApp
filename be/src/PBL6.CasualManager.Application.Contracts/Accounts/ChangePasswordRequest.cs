using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace PBL6.CasualManager.Accounts
{
    public class ChangePasswordRequest
    {
        public string NewPassword { get; set; }

        public string OldPassword { get; set; }
    }
}
