using PBL6.CasualManager.Districts;
using PBL6.CasualManager.ProvinceApi;
using PBL6.CasualManager.Provinces;
using PBL6.CasualManager.Wards;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Volo.Abp.Data;
using Volo.Abp.DependencyInjection;
using Volo.Abp.Domain.Repositories;
using Volo.Abp.Guids;
using Volo.Abp.Identity;

namespace PBL6.CasualManager
{
    public class CasualManagerDataSeederContributor : IDataSeedContributor, ITransientDependency
    {
        private readonly IRepository<Province> _provinceRepository;
        private readonly IRepository<District> _districtRepository;
        private readonly IRepository<Ward> _wardRepository;
        private readonly ProvinceApiManager _provinceApiManager;
        private readonly IRepository<IdentityRole, Guid> _roleRepository;
        private readonly IGuidGenerator _guidGenerator;

        public CasualManagerDataSeederContributor(
            IRepository<Province> provinceRepository,
            IRepository<District> districtRepository,
            IRepository<Ward> wardRepository,
            ProvinceApiManager provinceApiManager,
            IRepository<IdentityRole, Guid> roleRepository,
            IGuidGenerator guidGenerator) 
        {
            _provinceRepository = provinceRepository;
            _districtRepository = districtRepository;
            _wardRepository = wardRepository;
            _provinceApiManager = provinceApiManager;
            _roleRepository = roleRepository;
            _guidGenerator = guidGenerator;
        }
        public async Task SeedAsync(DataSeedContext context)
        {
            await SeedProvince();
            await SeedRole();
        }

        public async Task SeedProvince()
        {
            if (await _provinceRepository.AnyAsync())
            {
                return;
            };
            var provinceIncludes = await _provinceApiManager.GetInfoFromApi();
            var wards = new List<Ward>();
            foreach (var iProvince in provinceIncludes)
            {
                var province = await _provinceRepository.InsertAsync(new Province { Name = iProvince.Name });
                foreach (var iDistrict in iProvince.Districts)
                {
                    var district = await _districtRepository.InsertAsync(new District { Name = iDistrict.Name, ProvinceId = province.Id });
                    foreach (var ward in iDistrict.Wards)
                    {
                        wards.Add(new Ward { Name = ward.Name, DistrictId = district.Id });
                    }
                }
            }
            await _wardRepository.InsertManyAsync(wards);
        }

        public async Task SeedRole()
        {
            if (!await _roleRepository.AnyAsync(r => r.Name == Enum.Role.WORKER))
            {
                await _roleRepository.InsertAsync(
                    new IdentityRole(id: _guidGenerator.Create(), name: Enum.Role.WORKER),
                    autoSave: true
                );
            }
            if (!await _roleRepository.AnyAsync(r => r.Name == Enum.Role.CUSTOMER))
            {
                await _roleRepository.InsertAsync(
                    new IdentityRole(id: _guidGenerator.Create(), name: Enum.Role.CUSTOMER),
                    autoSave: true
                );
            }

        }
    }
}
