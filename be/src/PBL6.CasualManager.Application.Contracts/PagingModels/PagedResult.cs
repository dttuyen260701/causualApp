using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.PagingModels
{
    public class PagedResult<T> : PagedResultBase
    {
        public List<T> Items { set; get; }
    }
}
