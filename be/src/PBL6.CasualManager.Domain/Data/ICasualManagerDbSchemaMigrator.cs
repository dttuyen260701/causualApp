using System.Threading.Tasks;

namespace PBL6.CasualManager.Data;

public interface ICasualManagerDbSchemaMigrator
{
    Task MigrateAsync();
}
