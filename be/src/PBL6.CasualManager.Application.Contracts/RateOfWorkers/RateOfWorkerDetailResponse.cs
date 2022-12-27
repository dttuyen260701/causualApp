using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.RateOfWorkers
{
    public class RateOfWorkerDetailResponse
    {
        public float RateAverage { get; set; }
        public float AttitudeRateAverage { get; set; }
        public float SkillRateAverage { get; set; }
        public float PleasureRateAverage { get; set; }
        public float AttitudeRate { get; set; }
        public float SkillRate { get; set; }
        public float PleasureRate { get; set; }
        public string Comment { get; set; }
        public string CustomerImage { get; set; }
        public string CustomerName { get; set; }
        public string WorkerImage { get; set; }
        public string WorkerName { get; set; }
        public Guid OrderId { get; set; }
        public string CreationTime { get; set; }
    }
}
