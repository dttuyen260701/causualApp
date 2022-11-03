using System.Threading.Tasks;

namespace PBL6.CasualManager.CustomerInfos
{
    public interface ICustomerInfoAppService
    {
        Task<bool> AddAsync(CustomerInfoDto customerInfosDto);
    }
}