using Microsoft.EntityFrameworkCore;
using System.Linq;

namespace PBL6.CasualManager.JobInfos
{
    public static class JobInfoClassQuerableExtensions
    {
        public static IQueryable<JobInfo> IncludeDetails(
            this IQueryable<JobInfo> queryable,
            bool include = true)
        {
            if (!include)
            {
                return queryable;
            }

            return queryable
                .Include(x => x.TypeOfJob);
        }
    }
}
