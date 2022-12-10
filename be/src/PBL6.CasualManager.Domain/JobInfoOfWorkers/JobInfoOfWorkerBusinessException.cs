using Volo.Abp;

namespace PBL6.CasualManager.JobInfoOfWorkers
{
    public class JobInfoOfWorkerBusinessException
    {
        public class IsExistWorkerWithJobInfoException : BusinessException
        {
            public IsExistWorkerWithJobInfoException(string workerName, string jobName)
                : base(CasualManagerDomainErrorCodes.IsExistWorkerWithJobInfoException)
            {
                WithData("workerName", workerName);
                WithData("jobName", jobName);
            }
        }
    }
}
