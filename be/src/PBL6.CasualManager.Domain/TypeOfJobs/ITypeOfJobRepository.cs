using System;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Domain.Repositories;

namespace PBL6.CasualManager.TypeOfJobs
{
    public interface ITypeOfJobRepository : IRepository<TypeOfJob, Guid>
    {
        Task<PagedResultDto<TypeOfJob>> GetListByName(
            int skipCount,
            int maxResultCount,
            string sorting,
            string filterName);
    }
}
