using PBL6.CasualManager.WorkerInfos;
using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.WorkerResponses
{
    public class WorkerResponseVm
    {
        public Guid Id { get; set; }
        public WorkerInfoResponse WorkerInfo { get; set; }
        public string Time { get; set; }
    }
}
