using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.ApiResults
{
    public class ApiErrorResult<T> : ApiResult<T>
    {
        public Dictionary<string, string> ValidationErrors { get; set; }

        public ApiErrorResult()
        {
        }

        public ApiErrorResult(string message)
        {
            IsSuccessed = false;
            Message = message;
        }

        public ApiErrorResult(Dictionary<string, string> validationErrors)
        {
            IsSuccessed = false;
            ValidationErrors = validationErrors;
        }

        public ApiErrorResult(Dictionary<string, string> validationErrors, string message)
        {
            IsSuccessed = false;
            ValidationErrors = validationErrors;
            Message = message;
        }
    }
}
