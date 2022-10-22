using System;
using System.Threading.Tasks;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.DependencyInjection;
using PBL6.CasualManager.Data;
using Volo.Abp.DependencyInjection;

namespace PBL6.CasualManager.EntityFrameworkCore;

public class EntityFrameworkCoreCasualManagerDbSchemaMigrator
    : ICasualManagerDbSchemaMigrator, ITransientDependency
{
    private readonly IServiceProvider _serviceProvider;

    public EntityFrameworkCoreCasualManagerDbSchemaMigrator(
        IServiceProvider serviceProvider)
    {
        _serviceProvider = serviceProvider;
    }

    public async Task MigrateAsync()
    {
        /* We intentionally resolving the CasualManagerDbContext
         * from IServiceProvider (instead of directly injecting it)
         * to properly get the connection string of the current tenant in the
         * current scope.
         */

        await _serviceProvider
            .GetRequiredService<CasualManagerDbContext>()
            .Database
            .MigrateAsync();
    }
}
