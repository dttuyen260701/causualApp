using System;

namespace PBL6.CasualManager.Statisics
{
    public class TopProvinceDto
    {
        public Guid ProvinceId { get; set; }

        public string ProvinceName { get; set; }

        public int CustomerCount { get; set; }

        public int WorkerCount { get; set; }

        public int TotalUser { get; set; }
    }
}
