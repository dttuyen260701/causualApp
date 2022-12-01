using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.JobInfos
{
    public class JobInfoResponse
    {
        public Guid Id { get; set; }
        public string Name { get; set; }
        public int Price { get; set; }
        public string Description { get; set; }
        public Guid TypeOfJobId { get; set; }
        public string TypeOfJobName { get; set; }
        public string Image { get; set; }
    }
}
