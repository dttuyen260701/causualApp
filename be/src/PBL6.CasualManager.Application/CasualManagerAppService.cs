using System;
using System.Collections.Generic;
using System.Text;
using PBL6.CasualManager.Localization;
using Volo.Abp.Application.Services;

namespace PBL6.CasualManager;

/* Inherit your application services from this class.
 */
public abstract class CasualManagerAppService : ApplicationService
{
    protected CasualManagerAppService()
    {
        LocalizationResource = typeof(CasualManagerResource);
    }
}
