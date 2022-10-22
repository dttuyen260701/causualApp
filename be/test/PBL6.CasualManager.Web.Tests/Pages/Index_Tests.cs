using System.Threading.Tasks;
using Shouldly;
using Xunit;

namespace PBL6.CasualManager.Pages;

public class Index_Tests : CasualManagerWebTestBase
{
    [Fact]
    public async Task Welcome_Page()
    {
        var response = await GetResponseAsStringAsync("/");
        response.ShouldNotBeNull();
    }
}
