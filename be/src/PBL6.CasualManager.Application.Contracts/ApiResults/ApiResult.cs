using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.ApiResults
{
    public class ApiResult<T>
    {
        public bool IsSuccessed { get; set; }

        public string Message { get; set; }

        public T ResultObj { get; set; }
    }
}
