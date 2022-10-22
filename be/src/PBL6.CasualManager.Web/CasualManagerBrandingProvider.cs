using Volo.Abp.Ui.Branding;
using Volo.Abp.DependencyInjection;

namespace PBL6.CasualManager.Web;

[Dependency(ReplaceServices = true)]
public class CasualManagerBrandingProvider : DefaultBrandingProvider
{
    public override string AppName => "CasualManager";
}
