using AutoMapper.Internal.Mappers;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.Web.Pages.TypeOfJobs.TypeOfJobViewModels;
using System;
using System.Threading.Tasks;
using Volo.Abp.ObjectMapping;

namespace PBL6.CasualManager.Web.Pages.TypeOfJobs
{
    public class EditModalModel : CasualManagerPageModel
    {
        private readonly ITypeOfJobAppService _typeOfJobAppService;

        [BindProperty]
        public TypeOfJobCreateUpdateViewModel ViewModel { get; set; }

        public EditModalModel(ITypeOfJobAppService typeOfJobAppService)
        {
            _typeOfJobAppService = typeOfJobAppService;
        }

        public virtual async Task OnGetAsync(Guid id)
        {
            var typeOfJob = await _typeOfJobAppService.GetAsync(id);
            ViewModel = ObjectMapper.Map<TypeOfJobDto, TypeOfJobCreateUpdateViewModel>(typeOfJob);
        }

        public virtual async Task<ActionResult> OnPostAsync()
        {
            var createInfo = ObjectMapper.Map<TypeOfJobCreateUpdateViewModel, TypeOfJobCreateUpdateDto>(ViewModel);
            await _typeOfJobAppService.UpdateAsync(ViewModel.Id, createInfo);
            return NoContent();
        }
    }
}
