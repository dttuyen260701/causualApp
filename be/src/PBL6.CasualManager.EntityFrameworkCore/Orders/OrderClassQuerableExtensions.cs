using Microsoft.EntityFrameworkCore;
using System.Linq;

namespace PBL6.CasualManager.Orders
{
    public static class OrderClassQuerableExtensions
    {
        public static IQueryable<Order> IncludeDetails(
            this IQueryable<Order> queryable,
            bool include = true)
        {
            if (!include)
            {
                return queryable;
            }

            return queryable
                .Include(x => x.JobInfo).ThenInclude(x => x.TypeOfJob)
                .Include(x => x.CustomerInfo)
                .Include(x => x.WorkerInfo)
                .Include(x => x.PrieceDetail);
        }
    }
}
