using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using PBL6.CasualManager.Addresses;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.Extensions;
using PBL6.CasualManager.Web.Pages.WorkerInfos.WorkerViewModels;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;
using PBL6.CasualManager.WorkerInfos;

namespace PBL6.CasualManager.Web.Pages.WorkerInfos
{
    public class CreateModalModel : CasualManagerPageModel
    {
        private readonly IAddressAppService _addressAppService;
        private readonly IWorkerInfoAppService _workerInfoAppService;

        [BindProperty]
        public WorkerCreateViewModel ViewModel { get; set; }

        public List<SelectListItem> ListGenders { get; set; }

        public List<SelectListItem> ListProvinces { get; set; }

        public List<SelectListItem> ListDistricts { get; set; }

        public List<SelectListItem> ListWards { get; set; }

        public CreateModalModel(
            IAddressAppService addressAppService,
            IWorkerInfoAppService workerInfoAppService)
        {
            _addressAppService = addressAppService;
            _workerInfoAppService = workerInfoAppService;
        }

        public virtual async Task OnGetAsync()
        {
            ViewModel = new WorkerCreateViewModel();
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
            var workerCreate = ObjectMapper.Map<WorkerCreateViewModel, WorkerInfoCreateUpdateDto>(ViewModel);
            workerCreate.ProvinceName = (await _addressAppService.GetProvinceByIdAsync(Guid.Parse(workerCreate.ProvinceId))).Name;
            workerCreate.DistrictName = (await _addressAppService.GetDistrictByIdAsync(Guid.Parse(workerCreate.DistrictId))).Name;
            workerCreate.WardName = (await _addressAppService.GetWardByIdAsync(Guid.Parse(workerCreate.WardId))).Name;

            await _workerInfoAppService.CreateWorkerInfoAsync(workerCreate);
            return NoContent();
        }

        public class WorkerCreateViewModel : WorkerCreateUpdateViewModel
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
