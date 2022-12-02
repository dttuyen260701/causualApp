using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;

namespace PBL6.CasualManager.Addresses
{
    public interface IAddressAppService
    {
        Task<List<ProvinceDto>> GetProvinces();
        Task<List<DistrictDto>> GetDistricts(Guid id);//id = idProvince
        Task<List<WardDto>> GetWards(Guid id);//id = idDistrict

        Task<ProvinceDto> GetProvinceByIdAsync(Guid provinceId);

        Task<DistrictDto> GetDistrictByIdAsync(Guid districtId);

        Task<WardDto> GetWardByIdAsync(Guid wardId);
    }
}
