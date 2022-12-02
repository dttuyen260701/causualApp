using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using System.ComponentModel.DataAnnotations;
using System.Xml.Linq;

namespace PBL6.CasualManager.Web.Pages.WorkerInfos
{
    public class IndexModel : PageModel
    {
        public SearchViewModel ViewModel { get; set; }
        public void OnGet()
        {
            ViewModel = new SearchViewModel();
        }
        public class SearchViewModel
        {
            [Display(Name = "Worker:Key", Prompt = "Worker:EnterName")]
            public string FilterName { get; set; }
        }
    }
}
