using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.AspNetCore.Mvc.Rendering;
using PBL6.CasualManager.Addresses;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.Extensions;
using PBL6.CasualManager.Web.Pages.CustomerInfos.CustomerViewModels;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using static PBL6.CasualManager.Web.Pages.CustomerInfos.CreateModalModel;
using Volo.Abp.ObjectMapping;

namespace PBL6.CasualManager.Web.Pages.CustomerInfos
{
    public class EditModelModel : CasualManagerPageModel
    {

        private readonly IAddressAppService _addressAppService;
        private readonly ICustomerInfoAppService _customerInfoAppService;

        [BindProperty]
        public CustomerCreateUpdateViewModel ViewModel { get; set; }

        public List<SelectListItem> ListGenders { get; set; }

        public List<SelectListItem> ListProvinces { get; set; }

        public List<SelectListItem> ListDistricts { get; set; }

        public List<SelectListItem> ListWards { get; set; }

        public EditModelModel(
            IAddressAppService addressAppService,
            ICustomerInfoAppService customerInfoAppService)
        {
            _addressAppService = addressAppService;
            _customerInfoAppService = customerInfoAppService;
        }

        public virtual async Task OnGetAsync(Guid id)
        {
            var customerInfo = await _customerInfoAppService.GetCustomerInfoAsync(id);
            ViewModel = ObjectMapper.Map<CustomerInfoDto, CustomerCreateUpdateViewModel>(customerInfo);

            ListGenders = new List<SelectListItem>()
            {
                new SelectListItem() { Value = ((int)Gender.Male).ToString(), Text = L[EnumExtensions.GetDisplayName(Gender.Male)]},
                new SelectListItem() { Value = ((int)Gender.Female).ToString(), Text = L[EnumExtensions.GetDisplayName(Gender.Female)]},
                new SelectListItem() { Value = ((int)Gender.Undefined).ToString(), Text = L[EnumExtensions.GetDisplayName(Gender.Undefined)]},
            };
            ViewModel.Gender = customerInfo.Gender;

            var provinces = await _addressAppService.GetProvinces();
            ListProvinces = provinces.Select(x =>
                new SelectListItem()
                {
                    Selected = x.Id.ToString() == ViewModel.ProvinceId,
                    Value = x.Id.ToString(),
                    Text = x.Name
                }).ToList();

            Guid provinceId;
            if (!Guid.TryParse(customerInfo.ProvinceId, out provinceId))
            {
                provinceId = provinces.FirstOrDefault().Id;
            }
            var districts = await _addressAppService.GetDistricts(provinceId);
            ListDistricts = districts.Select(x =>
                new SelectListItem()
                {
                    Selected = x.Id.ToString() == ViewModel.DistrictId,
                    Value = x.Id.ToString(),
                    Text = x.Name
                }).ToList();

            Guid districtId;
            if (!Guid.TryParse(customerInfo.DistrictId, out districtId))
            {
                districtId = districts.FirstOrDefault().Id;
            }
            var wards = await _addressAppService.GetWards(districtId);
            ListWards = wards.Select(x =>
                new SelectListItem()
                {
                    Selected = x.Id.ToString() == ViewModel.WardId,
                    Value = x.Id.ToString(),
                    Text = x.Name
                }).ToList();
        }

        public virtual async Task<ActionResult> OnPostAsync()
        {
            var customerUpdate = ObjectMapper.Map<CustomerCreateUpdateViewModel, CustomerInfoCreateUpdateDto>(ViewModel);
            customerUpdate.ProvinceName = (await _addressAppService.GetProvinceByIdAsync(Guid.Parse(customerUpdate.ProvinceId))).Name;
            customerUpdate.DistrictName = (await _addressAppService.GetDistrictByIdAsync(Guid.Parse(customerUpdate.DistrictId))).Name;
            customerUpdate.WardName = (await _addressAppService.GetWardByIdAsync(Guid.Parse(customerUpdate.WardId))).Name;

            await _customerInfoAppService.UpdateCustomerInfoAsync(customerUpdate.Id, customerUpdate);
            return NoContent();
        }
    }
}
