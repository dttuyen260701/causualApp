using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.Orders
{
    public class OrderOfCustomerResponse
    {
        public Guid Id { get; set; }
        public Guid WorkerId { get; set; }
        public string WorkerName { get; set; }
        public Guid JobInfoId { get; set; }
        public string JobInfoName { get; set; }
        public string Note { get; set; }
        public string CreationTime { get; set; }
        public string TimeCompletion { get; set; }
        public string Price { get; set; }
        public string StatusPaid { get; set; }
    }
}
