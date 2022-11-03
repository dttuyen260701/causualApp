using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.ApiResults
{
    public class ApiErrorResult<T> : ApiResult<T>
    {
        public List<string> ValidationErrors { get; set; }

        public ApiErrorResult()
        {
        }

        public ApiErrorResult(string message)
        {
            IsSuccessed = false;
            Message = message;
        }

        public ApiErrorResult(List<string> validationErrors)
        {
            IsSuccessed = false;
            ValidationErrors = validationErrors;
        }
    }
}
