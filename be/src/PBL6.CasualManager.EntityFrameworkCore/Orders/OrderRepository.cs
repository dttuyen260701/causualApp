using Microsoft.EntityFrameworkCore;
using PBL6.CasualManager.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Domain.Repositories.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore;

namespace PBL6.CasualManager.Orders
{
    public class OrderRepository : EfCoreRepository<CasualManagerDbContext, Order, Guid>, IOrderRepository
    {
        public OrderRepository(IDbContextProvider<CasualManagerDbContext> dbContextProvider) : base(dbContextProvider)
        {

        }

        public async Task<PagedResultDto<Order>> GetListByUserAsync(
            int skipCount,
            int maxResultCount,
            string sorting,
            Guid? customerId = null,
            Guid? workerId = null)
        {
            PagedResultDto<Order> list = new PagedResultDto<Order>();
            var dbSet = await GetDbSetAsync();
            var query = dbSet
                .WhereIf(
                    customerId != null && customerId != Guid.Empty,
                    x => x.CustomerId.Equals(customerId)
                )
                .WhereIf(
                    workerId != null && workerId != Guid.Empty,
                    x => x.WorkerId.Equals(workerId)
                );
            list.Items = await query
                .IncludeDetails()
                .Skip(skipCount)
                .Take(maxResultCount)
                .OrderByDescending(x => x.CreationTime)
                .AsNoTracking()
                .ToListAsync();
            list.TotalCount = await query.CountAsync();
            return list;
        }

        public async Task<Dictionary<string, string>> GetListRevenueOfMonthsOfWorker(Guid workerId, int month = 1)
        {
            var dbSet = await GetDbSetAsync();
            var monthNow = DateTime.Now.Month;
            var query = dbSet
                .Include(x => x.PrieceDetail)
                .Where(x => x.Status == Enum.OrderStatus.IsComplete)
                .Where(x => x.WorkerId.Equals(workerId))
                .Where(x => x.CreationTime >= new DateTime(2022, monthNow - month + 1, 1)).ToList();

            return query
                .GroupBy(x => x.CreationTime.Month)
                .Select(x =>
                {
                    return new
                    {
                        Month = x.Key,
                        Revenue = x.Sum(c => c.PrieceDetail.FeeForWorker)
                    };
                })
                .ToDictionary(x => x.Month.ToString(), x => x.Revenue.ToString());
        }

        public async Task<Dictionary<string, string>> GetListRevenueOfMonthsOfBussiness(int month = 1)
        {
            var dbSet = await GetDbSetAsync();
            var monthNow = DateTime.Now.Month;
            var query = dbSet
                .Include(x => x.PrieceDetail)
                .Where(x => x.Status == Enum.OrderStatus.IsComplete)
                .Where(x => x.CreationTime >= new DateTime(2022, monthNow - month + 1, 1)).ToList();

            return query
                .GroupBy(x => x.CreationTime.Month)
                .Select(x =>
                {
                    return new
                    {
                        Month = x.Key,
                        Revenue = x.Sum(c => c.PrieceDetail.FeeForBussiness)
                    };
                })
                .ToDictionary(x => x.Month.ToString(), x => x.Revenue.ToString());
        }

        public async Task<Dictionary<string, Dictionary<string, string>>> GetListOrderOfMonthsOfWorker(Guid workerId, int month = 1)
        {
            var dbSet = await GetDbSetAsync();
            var monthNow = DateTime.Now.Month;
            var query = dbSet
                .Where(x => x.WorkerId.Equals(workerId))
                .Where(x => x.CreationTime >= new DateTime(2022, monthNow - month + 1, 1)).ToList();

            return query
                .GroupBy(x => x.CreationTime.Month)
                .Select(x =>
                {
                    return new
                    {
                        Month = x.Key,
                        Order = x.GroupBy(x => x.Status).Select(x => new { Status = x.Key, Count = x.Count() }).ToDictionary(x => x.Status.ToString(), x => x.Count.ToString())
                    };
                })
                .ToDictionary(x => x.Month.ToString(), x => x.Order);
        }

        public async Task<List<Order>> GetListNearest(int take)
        {
            var dbSet = await GetDbSetAsync();
            var result = await dbSet
                .IncludeDetails(true)
                .Take(take)
                .OrderByDescending(x => x.CreationTime)
                .AsNoTracking()
                .ToListAsync();
            return result;
        }

        public async Task<int> GetIncomeInDay(DateTime? date = null)
        {
            if(date == null)
            {
                date = DateTime.Now;
            }
            var dbSet = await GetDbSetAsync();
            return (int)dbSet
                .IncludeDetails(true)
                .Where(x => x.CreationTime.Equals(date))
                .Sum(x => x.PrieceDetail.FeeForBussiness);
        }

        public override async Task<IQueryable<Order>> WithDetailsAsync()
        {
            return (await GetQueryableAsync()).IncludeDetails();
        }
    }
}
