using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.RateOfWorkers
{
    public class CreateRateOfWorkerResponse
    {
        public Guid Id { get; set; }
        public string CustomerName { get; set; }
        public string WorkerName { get; set; }
        public Guid CustomerId { get; set; }
        public Guid WorkerId { get; set; }
        public int PleasureRate { get; set; }
        public int SkillRate { get; set; }
        public int AttitudeRate { get; set; }
        public string Comment { get; set; }
        public string CreationTime { get; set; }
        public Guid OrderId { get; set; }
        public float RateAverage { get; set; }
        public float AttitudeRateAverage { get; set; }
        public float SkillRateAverage { get; set; }
        public float PleasureRateAverage { get; set; }
    }
}
