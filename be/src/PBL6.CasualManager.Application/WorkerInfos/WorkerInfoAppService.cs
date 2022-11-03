using PBL6.CasualManager.CustomerInfos;
using System;
using System.Threading.Tasks;

namespace PBL6.CasualManager.WorkerInfos
{
    public class WorkerInfoAppService : CasualManagerAppService, IWorkerInfoAppService
    {
        private readonly IWorkerInfoRepository _workerInfoRepository;
        public WorkerInfoAppService(IWorkerInfoRepository workerInfoRepository)
        {
            _workerInfoRepository = workerInfoRepository;
        }

        public async Task<bool> AddAsync(WorkerInfoDto workerInfosDto)
        {
            var workerInfo = ObjectMapper.Map<WorkerInfoDto, WorkerInfo>(workerInfosDto);
            try
            {
                await _workerInfoRepository.InsertAsync(workerInfo, true);
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }
    }
}
