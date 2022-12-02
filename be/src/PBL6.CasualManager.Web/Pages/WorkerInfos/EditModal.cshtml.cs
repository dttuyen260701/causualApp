using Microsoft.AspNetCore.Mvc;
using System.Threading.Tasks;
using System;
using PBL6.CasualManager.Addresses;
using PBL6.CasualManager.WorkerInfos;
using Microsoft.AspNetCore.Mvc.Rendering;
using System.Collections.Generic;
using PBL6.CasualManager.Web.Pages.WorkerInfos.WorkerViewModels;
using PBL6.CasualManager.Enum;
using System.Linq;
using PBL6.CasualManager.Extensions;
using System.ComponentModel.DataAnnotations;
using System.Xml.Linq;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Web.Pages.CustomerInfos.CustomerViewModels;
using Volo.Abp.ObjectMapping;

namespace PBL6.CasualManager.Web.Pages.WorkerInfos
{
    public class EditModalModel : CasualManagerPageModel
    {
        private readonly IAddressAppService _addressAppService;
        private readonly IWorkerInfoAppService _workerInfoAppService;

        [BindProperty]
        public UpdateWorkerViewModel ViewModel { get; set; }

        public List<SelectListItem> ListGenders { get; set; }

        public List<SelectListItem> ListStatus { get; set; }

        public List<SelectListItem> ListProvinces { get; set; }

        public List<SelectListItem> ListDistricts { get; set; }

        public List<SelectListItem> ListWards { get; set; }

        public EditModalModel(
            IAddressAppService addressAppService,
            IWorkerInfoAppService workerInfoAppService)
        {
            _addressAppService = addressAppService;
            _workerInfoAppService = workerInfoAppService;
        }

        public virtual async Task OnGetAsync(Guid id)
        {
            var workerInfo = await _workerInfoAppService.GetWorkerInfoAsync(id);
            ViewModel = ObjectMapper.Map<WorkerInfoDto, UpdateWorkerViewModel>(workerInfo);

            ListGenders = new List<SelectListItem>()
            {
                new SelectListItem() { Value = ((int)Gender.Male).ToString(), Text = L[EnumExtensions.GetDisplayName(Gender.Male)]},
                new SelectListItem() { Value = ((int)Gender.Female).ToString(), Text = L[EnumExtensions.GetDisplayName(Gender.Female)]},
                new SelectListItem() { Value = ((int)Gender.Undefined).ToString(), Text = L[EnumExtensions.GetDisplayName(Gender.Undefined)]},
            };
            ViewModel.Gender = workerInfo.Gender;

            ListStatus = new List<SelectListItem>()
            {
                new SelectListItem() { Value = ((int)WorkerStatus.Free).ToString(), Text = L["Worker:Status:Free"]},
                new SelectListItem() { Value = ((int)WorkerStatus.Busy).ToString(), Text = L["Worker:Status:Busy"]}
            };
            ViewModel.Status = workerInfo.Status;

            var provinces = await _addressAppService.GetProvinces();
            ListProvinces = provinces.Select(x =>
                new SelectListItem()
                {
                    Selected = x.Id.ToString() == ViewModel.ProvinceId,
                    Value = x.Id.ToString(),
                    Text = x.Name
                }).ToList();

            Guid provinceId;
            if (!Guid.TryParse(workerInfo.ProvinceId, out provinceId))
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
            if (!Guid.TryParse(workerInfo.DistrictId, out districtId))
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
            var workerUpdate = ObjectMapper.Map<UpdateWorkerViewModel, WorkerInfoCreateUpdateDto>(ViewModel);
            workerUpdate.ProvinceName = (await _addressAppService.GetProvinceByIdAsync(Guid.Parse(workerUpdate.ProvinceId))).Name;
            workerUpdate.DistrictName = (await _addressAppService.GetDistrictByIdAsync(Guid.Parse(workerUpdate.DistrictId))).Name;
            workerUpdate.WardName = (await _addressAppService.GetWardByIdAsync(Guid.Parse(workerUpdate.WardId))).Name;

            await _workerInfoAppService.UpdateWorkerInfoAsync(workerUpdate.Id, workerUpdate);
            return NoContent();
        }

        public class UpdateWorkerViewModel : WorkerCreateUpdateViewModel
        {
            public int AverageRate { get; set; }

            [Display(Name = "Worker:Status")]
            public WorkerStatus Status { get; set; }
        }
    }
}
