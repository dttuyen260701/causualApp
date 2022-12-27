using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Domain.Repositories;

namespace PBL6.CasualManager.Orders
{
    public interface IOrderRepository : IRepository<Order, Guid>
    {
        Task<PagedResultDto<Order>> GetListByUserAsync(
            int skipCount,
            int maxResultCount,
            string sorting,
            Guid? customerId,
            Guid? workerId);

        Task<Dictionary<string, string>> GetListRevenueOfMonthsOfWorker(Guid workerId, int month = 1);

        Task<Dictionary<string, string>> GetListRevenueOfMonthsOfBussiness(int month = 1);

        Task<Dictionary<string, Dictionary<string, string>>> GetListOrderOfMonthsOfWorker(Guid workerId, int month = 1);

        Task<List<Order>> GetListNearest(int take);

        Task<int> GetIncomeInDay(DateTime? date = null);
    }
}
