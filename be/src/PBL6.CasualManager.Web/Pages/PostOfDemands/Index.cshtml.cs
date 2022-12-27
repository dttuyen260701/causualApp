using Microsoft.AspNetCore.Mvc.RazorPages;
using System.ComponentModel.DataAnnotations;
using System;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using System.Collections.Generic;
using Volo.Abp.AspNetCore.Mvc.UI.Bootstrap.TagHelpers.Form;
using PBL6.CasualManager.CustomerInfos;
using System.Linq;

namespace PBL6.CasualManager.Web.Pages.PostOfDemands
{
    public class IndexModel : CasualManagerPageModel
    {
        private readonly ICustomerInfoAppService _customerInfoAppService;

        [BindProperty]
        public SearchViewModel ViewModel { get; set; }

        public List<SelectListItem> ListCustomers { get; set; }

        public IndexModel(ICustomerInfoAppService customerInfoAppService)
        {
            _customerInfoAppService = customerInfoAppService;
        }

        public virtual async Task OnGetAsync(Guid customerId)
        {
            ViewModel = new SearchViewModel();
            var customers = await _customerInfoAppService.GetLookupValuesAsync();
            ListCustomers = new List<SelectListItem>()
            {
                new SelectListItem() { Value = Guid.Empty.ToString(), Text = L["Common:All"] }
            };
            ListCustomers.AddRange(customers.Select(i =>
                new SelectListItem()
                {
                    Value = i.Id.ToString(),
                    Text = i.Name
                }).ToList());
            if (customerId != Guid.Empty)
            {
                ViewModel.FilterCustomer = customerId;
            }
        }

        public class SearchViewModel
        {
            [Display(Name = "PostOfDemand:Search:Customer")]
            [SelectItems(nameof(ListCustomers))]
            public Guid FilterCustomer { get; set; }
        }
    }
}
