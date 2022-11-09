using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using System.ComponentModel.DataAnnotations;

namespace PBL6.CasualManager.Web.Pages.TypeOfJobs
{
    public class IndexModel : CasualManagerPageModel
    {
        [BindProperty]
        public SearchViewModel ViewModel { get; set; }

        public void OnGet()
        {
        }

        public class SearchViewModel
        {
            [Display(Name = "TypeOfJob:Name", Prompt = "TypeOfJob:EnterName")]
            public string FilterName { get; set; }
        }
    }
}
