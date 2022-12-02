using System.ComponentModel.DataAnnotations;

namespace PBL6.CasualManager.Web.Pages.CustomerInfos
{
    public class IndexModel : CasualManagerPageModel
    {
        public SearchViewModel ViewModel { get; set; }
        public void OnGet()
        {
            ViewModel = new SearchViewModel();
        }

        public class SearchViewModel
        {
            [Display(Name = "Customer:Key", Prompt = "Customer:EnterName")]
            public string FilterName { get; set; }
        }
    }
}
