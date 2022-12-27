using System;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Domain.Repositories;

namespace PBL6.CasualManager.PostOfDemands
{
    public interface IPostOfDemandRepository : IRepository<PostOfDemand, Guid>
    {
        Task<PagedResultDto<PostOfDemand>> GetListSearchAsync(
            int skipCount,
            int maxResultCount,
            string sorting,
            Guid? customerId);
    }
}
