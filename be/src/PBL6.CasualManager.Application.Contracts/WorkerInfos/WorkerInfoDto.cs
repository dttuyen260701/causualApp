﻿using PBL6.CasualManager.Enum;
using System;
using System.Collections.Generic;
using System.Text;

namespace PBL6.CasualManager.WorkerInfos
{
    public class WorkerInfoDto
    {
        public Guid UserId { get; set; }

        public string IdentityCard { get; set; }

        public Gender Gender { get; set; }

        public DateTime DateOfBirth { get; set; }

        public string Address { get; set; }

        public string AddressPoint { get; set; }

        public string Workingime { get; set; }

        public int AverageRate { get; set; }

    }

}
