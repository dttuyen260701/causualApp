using Volo.Abp;

namespace PBL6.CasualManager.CustomerInfos
{
    public class EmailExistException : BusinessException
    {
        public EmailExistException(string email)
            : base(CasualManagerDomainErrorCodes.EmailExistException)
        {
            WithData("email", email);
        }
    }

    public class PhoneExistException : BusinessException
    {
        public PhoneExistException(string phone)
            : base(CasualManagerDomainErrorCodes.PhoneExistException)
        {
            WithData("phone", phone);
        }
    }

    public class UserNameExistException : BusinessException
    {
        public UserNameExistException(string userName)
            : base(CasualManagerDomainErrorCodes.UserNameExistException)
        {
            WithData("userName", userName);
        }
    }

    public class InfoWrongException : BusinessException
    {
        public InfoWrongException()
            : base(CasualManagerDomainErrorCodes.InfoWrongException)
        {
        }
    }

    public class SetUpRoleException : BusinessException
    {
        public SetUpRoleException()
            : base(CasualManagerDomainErrorCodes.InfoWrongException)
        {
        }
    }
}
