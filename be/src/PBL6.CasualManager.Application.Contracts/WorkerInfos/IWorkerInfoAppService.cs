using PBL6.CasualManager.CustomerInfos;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace PBL6.CasualManager.WorkerInfos
{
    public interface IWorkerInfoAppService
    {
        Task<bool> AddAsync(WorkerInfoDto workerInfosDto);
    }
}
