using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;

namespace PBL6.CasualManager.PostOfDemands
{
    public class PostOfDemandDto
    {
        public Guid Id { get; set; }

        public string CustomerName { get; set; }

        public string JobName { get; set; }

        public string TypeOfJobIcon { get; set; }

        public string Description { get; set; }

        public string Note { get; set; }

        public bool IsActive { get; set; }

        public string CustomerAddressDetail { get; set; }

        public DateTime EndTime { get; set; }

        public string DistanceUpLoad { get; set; }
    }
}
