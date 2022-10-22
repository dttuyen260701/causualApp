using PBL6.CasualManager.Localization;
using Volo.Abp.AspNetCore.Mvc;

namespace PBL6.CasualManager.Controllers;

/* Inherit your controllers from this class.
 */
public abstract class CasualManagerController : AbpControllerBase
{
    protected CasualManagerController()
    {
        LocalizationResource = typeof(CasualManagerResource);
    }
}
