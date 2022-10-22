using System.Threading.Tasks;
using Volo.Abp.DependencyInjection;

namespace PBL6.CasualManager.Data;

/* This is used if database provider does't define
 * ICasualManagerDbSchemaMigrator implementation.
 */
public class NullCasualManagerDbSchemaMigrator : ICasualManagerDbSchemaMigrator, ITransientDependency
{
    public Task MigrateAsync()
    {
        return Task.CompletedTask;
    }
}
