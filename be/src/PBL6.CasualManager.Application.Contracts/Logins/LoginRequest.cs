using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.Logins
{
    public class LoginRequest
    {
        public string Email { get; set; }
        public string Password { get; set; }
        public bool Rememberme { get; set; }
    }
}
