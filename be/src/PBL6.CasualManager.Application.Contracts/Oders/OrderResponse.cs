using PBL6.CasualManager.Enum;
using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.Orders
{
    public class OrderResponse
    {
        public Guid Id { get; set; }
        public Guid JobId { get; set; }
        public Guid CustomerId { get; set; }
        public Guid WorkerId { get; set; }
        public string Note { get; set; }
        public string JobPrices { get; set; }
        public string CreationTime { get; set; }
        public string UserAddress { get; set; }
        public string UserPoint { get; set; }
        public bool IsPaid { get; set; }
        public OrderStatus Status { get; set; }
        public string JobInfoName { get; set; }
        public string JobInfoImage { get; set; }
        public string CustomerName { get; set; }
        public string CustomerImage { get; set; }
        public string CustomerPhone { get; set; }
        public string WorkerName { get; set; }
        public string WorkerImage { get; set; }
        public string WorkerPhone { get; set; }
        public bool IsRead { get; set; }

    }
}
