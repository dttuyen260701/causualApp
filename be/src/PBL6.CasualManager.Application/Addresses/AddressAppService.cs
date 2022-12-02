using PBL6.CasualManager.Districts;
using PBL6.CasualManager.Provinces;
using PBL6.CasualManager.Wards;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace PBL6.CasualManager.Addresses
{
    public class AddressAppService : CasualManagerAppService, IAddressAppService
    {
        private readonly IProvinceRepository _provinceRepository;
        private readonly IDistrictRepository _districtRepository;
        private readonly IWardRepository _wardRepository;
        public AddressAppService(IProvinceRepository provinceRepository, IDistrictRepository districtRepository, IWardRepository wardRepository)
        {
            _provinceRepository = provinceRepository;
            _districtRepository = districtRepository;   
            _wardRepository = wardRepository;   
        }

        public async Task<List<DistrictDto>> GetDistricts(Guid id)
        {
            var districts = await _districtRepository.GetDistrictsBelongProvinceAsync(id);
            var districtsDto = new List<DistrictDto>();
            foreach(var district in districts)
            {
                districtsDto.Add(new DistrictDto() 
                { 
                    Id = district.Id,
                    Name = district.Name
                });
            }
            return districtsDto;
        }

        public async Task<List<ProvinceDto>> GetProvinces()
        {
            var allProvince = await _provinceRepository.GetListAsync();
            var allProvinceDto = new List<ProvinceDto>();
            foreach (var province in allProvince)
            {
                allProvinceDto.Add(new ProvinceDto() 
                { 
                    Id = province.Id,
                    Name = province.Name,
                });
            }
            return allProvinceDto;
        }

        public async Task<List<WardDto>> GetWards(Guid id)
        {
            var wards = await _wardRepository.GetWardsBelongDistrict(id);
            var wardsDto = new List<WardDto>();
            foreach(var ward in wards)
            {
                wardsDto.Add(new WardDto()
                {
                    Id = ward.Id,
                    Name = ward.Name
                });
            }
            return wardsDto;
        }

        public async Task<ProvinceDto> GetProvinceByIdAsync(Guid provinceId)
        {
            return ObjectMapper.Map<Province, ProvinceDto>(await _provinceRepository.GetAsync(provinceId));
        }

        public async Task<DistrictDto> GetDistrictByIdAsync(Guid districtId)
        {
            return ObjectMapper.Map<District, DistrictDto>(await _districtRepository.GetAsync(districtId));
        }

        public async Task<WardDto> GetWardByIdAsync(Guid wardId)
        {
            return ObjectMapper.Map<Ward, WardDto>(await _wardRepository.GetAsync(wardId));
        }
    }
}
