using System.Collections.Generic;
using System.Threading.Tasks;

namespace PBL6.CasualManager.LookupValues
{
    public interface ILookupValueService
    {
        public Task<List<LookupValueDto>> GetLookupValuesAsync();
    }
}
