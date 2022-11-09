using System;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.LookupValues
{
    public class LookupValueDto : EntityDto<Guid>
    {
        public string Name { get; set; }
    }
}
