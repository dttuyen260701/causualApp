﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Domain.Repositories;

namespace PBL6.CasualManager.JobInfoOfWorkers
{
    public interface IJobInfoOfWorkerRepository : IRepository<JobInfoOfWorker, Guid>
    {
    }
}
