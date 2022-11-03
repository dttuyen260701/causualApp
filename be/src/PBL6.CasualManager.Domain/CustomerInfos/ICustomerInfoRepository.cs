using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Domain.Repositories;

namespace PBL6.CasualManager.CustomerInfos
{
    public interface ICustomerInfoRepository : IRepository<CustomerInfo, Guid>
    {
        Task<CustomerInfo> GetEntityCustomerInfoHaveUserId(Guid userId);
    }
}
