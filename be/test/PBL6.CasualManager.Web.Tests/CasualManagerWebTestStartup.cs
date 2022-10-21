using System;
using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Volo.Abp;

namespace PBL6.CasualManager;

public class CasualManagerWebTestStartup
{
    public void ConfigureServices(IServiceCollection services)
    {
        services.AddApplication<CasualManagerWebTestModule>();
    }

    public void Configure(IApplicationBuilder app, ILoggerFactory loggerFactory)
    {
        app.InitializeApplication();
    }
}
