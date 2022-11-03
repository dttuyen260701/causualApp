using PBL6.CasualManager.Accounts;
using PBL6.CasualManager.ApiResults;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace PBL6.CasualManager.Logins
{
    public interface ILoginAppService
    {
        Task<ApiResult<UserInfoAllDto>> PostLogin(LoginRequest request);
    }
}
