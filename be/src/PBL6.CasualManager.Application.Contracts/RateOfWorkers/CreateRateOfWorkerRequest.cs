using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.RateOfWorkers
{
    public class CreateRateOfWorkerRequest
    {
        public Guid OrderId { get; set; }
        public Guid WorkerId { get; set; }
        public string Comment { get; set; }
        public int AttitudeRate { get; set; }
        public int SkillRate { get; set; }
        public int PleasureRate { get; set; }
    }
}
