using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.FileStorages;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.Web.Pages.TypeOfJobs.TypeOfJobViewModels;
using System;
using System.Threading.Tasks;

namespace PBL6.CasualManager.Web.Pages.TypeOfJobs
{
    public class CreateModalModel : CasualManagerPageModel
    {
        private readonly ITypeOfJobAppService _typeOfJobAppService;
        private readonly IFileStorageAppService _fileStorageAppService;

        [BindProperty]
        public TypeOfJobCreateUpdateViewModel ViewModel { get; set; }

        public IFormFile FileIcon { get; set; }

        public CreateModalModel(
            ITypeOfJobAppService typeOfJobAppService,
            IFileStorageAppService fileStorageAppService)
        {
            _typeOfJobAppService = typeOfJobAppService;
            _fileStorageAppService = fileStorageAppService;
        }

        public virtual async Task OnGetAsync()
        {
            ViewModel = new TypeOfJobCreateUpdateViewModel();
        }

        public virtual async Task<ActionResult> OnPostAsync()
        {
            
            var createInfo = ObjectMapper.Map<TypeOfJobCreateUpdateViewModel, TypeOfJobCreateUpdateDto>(ViewModel);
            createInfo.Id = Guid.NewGuid();
            var path = $"{Constants.LinkToFolderIconTypeOfJob}{Constants.PrefixIconTypeOfJob}{createInfo.Id}.png";
            createInfo.Avatar = await _fileStorageAppService.SaveImageAsync(FileIcon, path) ? path : null;
            await _typeOfJobAppService.CreateAsync(createInfo);
            return NoContent();
        }
    }
}
