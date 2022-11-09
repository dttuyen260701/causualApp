using System;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Domain.Repositories;

namespace PBL6.CasualManager.JobInfos
{
    public interface IJobInfoRepository : IRepository<JobInfo, Guid>
    {
        Task<PagedResultDto<JobInfo>> GetListSearchAsync(
            int skipCount,
            int maxResultCount,
            string sorting,
            string filterName,
            Guid? filterTypeOfJob);
    }
}
