using System;

namespace PBL6.CasualManager.Web.Pages.WorkerInfos.WorkerViewModels
{
    public class WorkerInfoDetailViewModel
    {
        public Guid WorkerId { get; set; }

        public string Name { get; set; }

        public string Phone { get; set; }

        public string Email { get; set; }

        public string DateOfBirth { get; set; }

        public string GenderName { get; set; }

        public string UserName { get; set; }

        public string AddressDetail { get; set; }

        public string IdentityCard { get; set; }

        public string IdentityCardDate { get; set; }

        public string IdentityCardBy { get; set; }

        public int AverageRate { get; set; }

        public string WorkingTime { get; set; }

        public bool IsActive { get; set; }
    }
}
