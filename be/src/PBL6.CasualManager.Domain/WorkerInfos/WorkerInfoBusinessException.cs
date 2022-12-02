using System.Numerics;
using Volo.Abp;

namespace PBL6.CasualManager.WorkerInfos
{
    public class WorkerInfoBusinessException
    {
        public class IdentityCardExistException : BusinessException
        {
            public IdentityCardExistException(string identityCard)
                : base(CasualManagerDomainErrorCodes.IdentityCardExistException)
            {
                WithData("identityCard", identityCard);
            }
        }
    }
}
