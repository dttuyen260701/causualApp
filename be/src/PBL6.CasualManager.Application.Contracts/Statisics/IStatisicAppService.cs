using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.Oders;
using PBL6.CasualManager.WorkerInfos;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace PBL6.CasualManager.Statisics
{
    public interface IStatisicAppService
    {
        Task<Dictionary<string, Dictionary<string, string>>> GetListRevenueOfMonthsOfBussinessAsync(int month = 1);

        Task<List<DifferentialRevenueDto>> CalculatingDifferentialRevenue(int month);

        Task<List<TopProvinceDto>> GetTopProvinceMostUser(int take);

        Task<List<WorkerInfoDto>> GetTopWorker(int take);

        Task<List<CustomerInfoDto>> GetTopCustomer(int take);

        Task<List<OrderDto>> GetNearestOrder(int take);

        Task<List<UserInfoDto>> GetNearestUser(int take);

        Task<List<JobInfoDto>> GetNewestJobAdded(int take);

        Task<int> GetCountOfCustomer();

        Task<int> GetCountOfWorker();

        Task<int> GetCountOfOrderIsInProcess();

        Task<int> GetIncomeInDay();
    }
}
