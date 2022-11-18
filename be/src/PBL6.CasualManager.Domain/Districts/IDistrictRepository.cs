using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Domain.Repositories;

namespace PBL6.CasualManager.Districts
{
    public interface IDistrictRepository : IRepository<District, Guid>
    {
        Task<List<District>> GetDistrictsBelongProvinceAsync(Guid idProvince);
    }
}
