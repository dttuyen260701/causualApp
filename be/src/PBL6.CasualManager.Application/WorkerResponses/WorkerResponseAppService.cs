using PBL6.CasualManager.ApiResults;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PBL6.CasualManager.WorkerResponses
{
    public class WorkerResponseAppService : CasualManagerAppService, IWorkerResponseAppService
    {
        private readonly IWorkerResponseRepository _workerResponseRepository;
        public WorkerResponseAppService(IWorkerResponseRepository workerResponseRepository)
        {
            _workerResponseRepository = workerResponseRepository;
        }

    }
}
