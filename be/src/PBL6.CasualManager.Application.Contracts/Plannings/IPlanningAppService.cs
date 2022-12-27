using System.Collections.Generic;
using System.Threading.Tasks;

namespace PBL6.CasualManager.Plannings
{
    public interface IPlanningAppService
    {
        Task<Dictionary<string, string>> GetListRevenueTargetOfMonthsAsync(int month = 1, int year = 0);
    }
}
