using Microsoft.EntityFrameworkCore;
using System.Linq;

namespace PBL6.CasualManager.JobInfoOfWorkers
{
    public static class JobInfoOfWorkerQuerableExtensions
    {
        public static IQueryable<JobInfoOfWorker> IncludeDetails(
            this IQueryable<JobInfoOfWorker> queryable,
            bool include = true)
        {
            if (!include)
            {
                return queryable;
            }

            return queryable
                .Include(x => x.JobInfo)
                .Include(x => x.WorkerInfo);
        }
    }
}
