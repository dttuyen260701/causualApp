using System;
using System.Collections.Generic;
using System.Text;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.JobInfoOfWorkers
{
    public class JobInfoOfWorkerConditionSearchDto : PagedAndSortedResultRequestDto
    {
        public Guid? FilterWorker { get; set; }
        public string FilterJobName { get; set; }
        public Guid? FilterTypeOfJob { get; set; }
    }
}
