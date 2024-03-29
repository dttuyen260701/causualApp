﻿using PBL6.CasualManager.Enum;
using System;
using Volo.Abp.Application.Dtos;

namespace PBL6.CasualManager.CustomerInfos
{
    public class CustomerInfoDto : FullAuditedEntityDto<Guid>
    {
        public Guid UserId { get; set; }

        public Gender Gender { get; set; }

        public string GenderName { get; set; }
        
        public string Address { get; set; }

        public string AddressPoint { get; set; }

        public string Name { get; set; }

        public string UserName { get; set; }

        public string Phone { get; set; }

        public string Email { get; set; }

        public DateTime DateOfBirth { get; set; }

        public string ProvinceId { get; set; }

        public string ProvinceName { get; set; }

        public string DistrictId { get; set; }

        public string DistrictName { get; set; }

        public string WardId { get; set; }

        public string WardName { get; set; }

        public string AddressDetail { get; set; }

        public int CountOfOrder { get; set; }

        public string Avatar { get; set; }
    }

}
