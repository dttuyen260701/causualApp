using PBL6.CasualManager.Localization;
using Volo.Abp.AspNetCore.Mvc.UI.RazorPages;

namespace PBL6.CasualManager.Web.Pages;

/* Inherit your PageModel classes from this class.
 */
public abstract class CasualManagerPageModel : AbpPageModel
{
    protected CasualManagerPageModel()
    {
        LocalizationResourceType = typeof(CasualManagerResource);
    }
}
