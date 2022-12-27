using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.PostOfDemands
{
    public interface IPostOfDemandAppService
    {
        Task<PagedResultDto<PostOfDemandDto>> GetListSearchAsync(PostOfDemandConditionSearchDto condition);
    }
}
