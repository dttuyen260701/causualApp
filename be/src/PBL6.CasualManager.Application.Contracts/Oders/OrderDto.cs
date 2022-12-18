using PBL6.CasualManager.Enum;
using System;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.Oders
{
    public class OrderDto : FullAuditedEntityDto<Guid>
    {
        public DateTime OrderDate { get; set; }

        public string CustomerName { get; set; }

        public string CustomerAddress { get; set; }

        public string WorkerName { get; set; }

        public string JobName { get; set; }

        public string IconTypeOfJob { get; set; }

        public string Note { get; set; }

        public float JobPrices { get; set; }

        public float FeeForWorker { get; set; }

        public OrderStatus Status { get; set; }

        public string StatusStr { get; set; }

        public bool IsPaid { get; set; }
    }
}
