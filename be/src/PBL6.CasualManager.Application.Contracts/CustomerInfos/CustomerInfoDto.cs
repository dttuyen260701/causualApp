using PBL6.CasualManager.Enum;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace PBL6.CasualManager.CustomerInfos
{
    public class CustomerInfoDto
    {
        public Guid UserId { get; set; }

        public Gender Gender { get; set; }

        public string Address { get; set; }

        public string AddressPoint { get; set; }
    }
}
