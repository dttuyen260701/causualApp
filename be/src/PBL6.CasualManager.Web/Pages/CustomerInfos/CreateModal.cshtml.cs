using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using PBL6.CasualManager.Addresses;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.Extensions;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.Web.Pages.CustomerInfos.CustomerViewModels;
using PBL6.CasualManager.Web.Pages.TypeOfJobs.TypeOfJobViewModels;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;
using System.Xml.Linq;
using Volo.Abp.ObjectMapping;

namespace PBL6.CasualManager.Web.Pages.CustomerInfos
{
    public class CreateModalModel : CasualManagerPageModel
    {
        private readonly IAddressAppService _addressAppService;
        private readonly ICustomerInfoAppService _customerInfoAppService;

        [BindProperty]
        public CustomerCreateViewModel ViewModel { get; set; }

        public List<SelectListItem> ListGenders { get; set; }
        
        public List<SelectListItem> ListProvinces { get; set; }
        
        public List<SelectListItem> ListDistricts { get; set; }
        
        public List<SelectListItem> ListWards { get; set; }

        public CreateModalModel(
            IAddressAppService addressAppService,
            ICustomerInfoAppService customerInfoAppService)
        {
            _addressAppService = addressAppService;
            _customerInfoAppService = customerInfoAppService;
        }

        public virtual async Task OnGetAsync()
        {
            ViewModel = new CustomerCreateViewModel();
            ListGenders = new List<SelectListItem>()
            {
                new SelectListItem() { Value = ((int)Gender.Male).ToString(), Text = L[EnumExtensions.GetDisplayName(Gender.Male)]},
                new SelectListItem() { Value = ((int)Gender.Female).ToString(), Text = L[EnumExtensions.GetDisplayName(Gender.Female)]},
                new SelectListItem() { Value = ((int)Gender.Undefined).ToString(), Text = L[EnumExtensions.GetDisplayName(Gender.Undefined)]},
            };

            var provinces = await _addressAppService.GetProvinces();
            ListProvinces = provinces.Select(x =>
                new SelectListItem()
                {
                    Value = x.Id.ToString(),
                    Text = x.Name
                }).ToList();

            var districts = await _addressAppService.GetDistricts(provinces.FirstOrDefault().Id);
            ListDistricts = districts.Select(x =>
                new SelectListItem()
                {
                    Value = x.Id.ToString(),
                    Text = x.Name
                }).ToList();

            var wards = await _addressAppService.GetWards(districts.FirstOrDefault().Id);
            ListWards = wards.Select(x =>
                new SelectListItem()
                {
                    Value = x.Id.ToString(),
                    Text = x.Name
                }).ToList();
        }

        public virtual async Task<ActionResult> OnPostAsync()
        {
            var customerCreate = ObjectMapper.Map<CustomerCreateViewModel, CustomerInfoCreateUpdateDto>(ViewModel);
            customerCreate.ProvinceName = (await _addressAppService.GetProvinceByIdAsync(Guid.Parse(customerCreate.ProvinceId))).Name;
            customerCreate.DistrictName = (await _addressAppService.GetDistrictByIdAsync(Guid.Parse(customerCreate.DistrictId))).Name;
            customerCreate.WardName = (await _addressAppService.GetWardByIdAsync(Guid.Parse(customerCreate.WardId))).Name;

            await _customerInfoAppService.CreateCustomerInfoAsync(customerCreate);
            return NoContent();
        }

        public class CustomerCreateViewModel : CustomerCreateUpdateViewModel
        {
            [Required]
            [DataType(DataType.Password)]
            [Display(Name = "Common:Password")]
            public string Password { get; set; }

            [Required]
            [DataType(DataType.Password)]
            [Compare("Password")]
            [Display(Name = "Common:ComfirmPassword")]
            public string ComfirmPassword { get; set; }
        }
    }
}
