using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.FileStorages;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.Web.Pages.TypeOfJobs.TypeOfJobViewModels;
using System;
using System.IO;
using System.Threading.Tasks;

namespace PBL6.CasualManager.Web.Pages.TypeOfJobs
{
    public class EditModalModel : CasualManagerPageModel
    {
        private readonly ITypeOfJobAppService _typeOfJobAppService;
        private readonly IFileStorageAppService _fileStorageAppService;

        [BindProperty]
        public TypeOfJobCreateUpdateViewModel ViewModel { get; set; }

        public IFormFile FileIcon { get; set; }

        public EditModalModel(
            ITypeOfJobAppService typeOfJobAppService,
            IFileStorageAppService fileStorageAppService)
        {
            _typeOfJobAppService = typeOfJobAppService;
            _fileStorageAppService = fileStorageAppService;
        }

        public virtual async Task OnGetAsync(Guid id)
        {
            var typeOfJob = await _typeOfJobAppService.GetAsync(id);
            ViewModel = ObjectMapper.Map<TypeOfJobDto, TypeOfJobCreateUpdateViewModel>(typeOfJob);
        }

        public virtual async Task<ActionResult> OnPostAsync()
        {
            if (FileIcon != null)
            {
                var newAvatarPath = $"{Constants.LinkToFolderIconTypeOfJob}{Constants.PrefixIconTypeOfJob}{ViewModel.Id}.png";
                if (_fileStorageAppService.DeleteImageAsync(ViewModel.Avatar))
                {
                    await _fileStorageAppService.SaveImageAsync(FileIcon, newAvatarPath);
                    ViewModel.Avatar = newAvatarPath;
                }
            }
            var updateInfo = ObjectMapper.Map<TypeOfJobCreateUpdateViewModel, TypeOfJobCreateUpdateDto>(ViewModel);
            await _typeOfJobAppService.UpdateAsync(ViewModel.Id, updateInfo);
            return NoContent();
        }
    }
}
