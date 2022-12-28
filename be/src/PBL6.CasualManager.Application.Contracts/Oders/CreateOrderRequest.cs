using PBL6.CasualManager.Enum;
using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.Orders
{
    public class CreateOrderRequest
    {
        public Guid WorkerId { get; set; }
        public Guid JobInfoId { get; set; }
        public string Note { get; set; }
        public string AddressPoint { get; set; }
        public string Address { get; set; }
        public OrderStatus Status { get; set; }

    }
}
