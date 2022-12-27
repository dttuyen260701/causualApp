using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PBL6.CasualManager.Plannings
{
    public class PlanningAppService : IPlanningAppService
    {
        private readonly IPlanningRepository _planningRepository;

        public PlanningAppService(IPlanningRepository planningRepository)
        {
            _planningRepository = planningRepository;
        }

        public async Task<Dictionary<string, string>> GetListRevenueTargetOfMonthsAsync(int month = 1, int year = 0)
        {
            return await _planningRepository.GetListRevenueTargetOfMonths(month, year);
        }
    }
}
