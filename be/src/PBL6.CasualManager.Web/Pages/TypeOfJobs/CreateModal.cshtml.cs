using Microsoft.AspNetCore.Mvc;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.Web.Pages.TypeOfJobs.TypeOfJobViewModels;
using System.Threading.Tasks;

namespace PBL6.CasualManager.Web.Pages.TypeOfJobs
{
    public class CreateModalModel : CasualManagerPageModel
    {
        private readonly ITypeOfJobAppService _typeOfJobAppService;

        [BindProperty]
        public TypeOfJobCreateUpdateViewModel ViewModel { get; set; }

        public CreateModalModel(ITypeOfJobAppService typeOfJobAppService)
        {
            _typeOfJobAppService = typeOfJobAppService;
        }

        public virtual async Task OnGetAsync()
        {
            ViewModel = new TypeOfJobCreateUpdateViewModel();
        }

        public virtual async Task<ActionResult> OnPostAsync()
        {
            var createInfo = ObjectMapper.Map<TypeOfJobCreateUpdateViewModel, TypeOfJobCreateUpdateDto>(ViewModel);
            await _typeOfJobAppService.CreateAsync(createInfo);
            return NoContent();
        }
    }
}
