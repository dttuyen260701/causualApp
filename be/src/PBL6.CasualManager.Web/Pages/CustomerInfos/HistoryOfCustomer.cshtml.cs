using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using PBL6.CasualManager.CustomerInfos;
using System;
using System.Threading.Tasks;

namespace PBL6.CasualManager.Web.Pages.CustomerInfos
{
    public class HistoryOfCustomerModel : PageModel
    {
        private readonly ICustomerInfoAppService _customerInfoAppService;

        public SearchModel ViewModel { get; set; }

        public HistoryOfCustomerModel(ICustomerInfoAppService customerInfoAppService)
        {
            _customerInfoAppService = customerInfoAppService;
        }

        public virtual async Task OnGetAsync(Guid id)
        {
            var customer = await _customerInfoAppService.GetCustomerInfoAsync(id);

            ViewModel = new SearchModel 
            { 
                CustomerId = id,
                CustomerName = customer.Name
            };
        }

        public class SearchModel
        {
            public Guid CustomerId { get; set; }

            public string CustomerName { get; set; }
        }
    }
}
