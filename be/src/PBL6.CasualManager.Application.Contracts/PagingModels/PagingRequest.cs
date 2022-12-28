using PBL6.CasualManager.Enum;
using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.PagingModels
{
    public class PagingRequest
    {
        public int PageIndex { get; set; } = DefaultPaging.PageIndex;
        public int PageSize { get; set; } = DefaultPaging.PageSize;
        public string Keyword { get; set; } = DefaultPaging.Keyword;
    }
}
