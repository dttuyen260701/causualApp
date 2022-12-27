﻿using Microsoft.EntityFrameworkCore;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Domain.Repositories.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore;

namespace PBL6.CasualManager.WorkerInfos
{
    public class WorkerInfoRepository : EfCoreRepository<CasualManagerDbContext, WorkerInfo, Guid>, IWorkerInfoRepository
    {
        public WorkerInfoRepository(IDbContextProvider<CasualManagerDbContext> dbContextProvider) : base(dbContextProvider)
        {
        }
        public async Task<WorkerInfo> GetEntityWorkerInfoHaveUserId(Guid userId)
        {
            return await FindAsync(x => x.UserId == userId);
        }
        public async Task<bool> CheckExistIdentityCard(string IdentityCard)
        {
            var result = await FindAsync(x => x.IdentityCard == IdentityCard);  
            if(result != null)
            {
                return true;
            }
            return false;
        }

        public async Task<List<WorkerInfo>> GetListTopWorker(int take)
        {
            var dbSet = await GetDbSetAsync();
            var result = await dbSet
                .Include(x => x.RateOfWorkers)
                .OrderByDescending(x => x.AverageRate)
                .ThenByDescending(x => x.RateOfWorkers.Count)
                .Take(take)
                .AsNoTracking()
                .ToListAsync();
            return result;
        }
    }
}
