using System;
using System.Collections.Generic;
using System.Security.Principal;
using System.Text;

namespace PBL6.CasualManager.PostOfDemands
{
    public class CreatePostOfDemandRequest
    {
        public Guid UserId { get; set; }
        public Guid JobInfoId { get; set; }
        public string Description { get; set; }
        public string Note { get; set; }
        public string EndTime { get; set; }
        public string Address { get; set; }
        public string AddressPoint { get; set; }
    }
}
