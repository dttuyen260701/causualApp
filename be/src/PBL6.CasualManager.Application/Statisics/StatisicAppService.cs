using System.Collections.Generic;
using System.Threading.Tasks;
using System;
using PBL6.CasualManager.Plannings;
using PBL6.CasualManager.Orders;
using PBL6.CasualManager.WorkerInfos;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Oders;
using Volo.Abp.Identity;
using PBL6.CasualManager.Enum;
using System.Linq;
using PBL6.CasualManager.JobInfos;
using Volo.Abp.Domain.Repositories;
using PBL6.CasualManager.Addresses;
using Microsoft.AspNetCore.Authorization;
using PBL6.CasualManager.Permissions;

namespace PBL6.CasualManager.Statisics
{
    public class StatisicAppService : CasualManagerAppService, IStatisicAppService
    {
        private readonly IIdentityUserRepository _identityUserRepository;
        private readonly IdentityUserManager _identityUserManager;
        private readonly IOrderRepository _orderRepository;
        private readonly IPlanningRepository _planningRepository;
        private readonly IWorkerInfoRepository _workerInfoRepository;
        private readonly ICustomerInfoRepository _customerInfoRepository;
        private readonly IJobInfoRepository _jobInfoRepository;
        private readonly IAddressAppService _addressAppService;

        public StatisicAppService(
            IIdentityUserRepository identityUserRepository,
            IdentityUserManager identityUserManager,
            IOrderRepository orderRepository,
            IPlanningRepository planningRepository,
            IWorkerInfoRepository workerInfoRepository,
            ICustomerInfoRepository customerInfoRepository,
            IJobInfoRepository jobInfoRepository,
            IAddressAppService addressAppService)
        {
            _identityUserRepository = identityUserRepository;
            _identityUserManager = identityUserManager;
            _orderRepository = orderRepository;
            _planningRepository = planningRepository;
            _workerInfoRepository = workerInfoRepository;
            _customerInfoRepository = customerInfoRepository;
            _jobInfoRepository = jobInfoRepository;
            _addressAppService = addressAppService;
        }

        [Authorize(CasualManagerPermissions.Statisic.Default)]
        public async Task<Dictionary<string, Dictionary<string, string>>> GetListRevenueOfMonthsOfBussinessAsync(int month = 1)
        {
            var targets = await _planningRepository.GetListRevenueTargetOfMonths(month);
            var revenues = await _orderRepository.GetListRevenueOfMonthsOfBussiness(month);
            var result = new Dictionary<string, Dictionary<string, string>>();
            var monthNow = DateTime.Now.Month;
            for (var i = monthNow - month + 1; i <= monthNow; i++)
            {
                string revenue;
                if (!revenues.TryGetValue(i.ToString(), out revenue))
                {
                    revenue = "0";
                }
                string target;
                if (!targets.TryGetValue(i.ToString(), out target))
                {
                    target = "0";
                }
                result.Add(i + "", new Dictionary<string, string>() { { "revenue", revenue }, { "target", target } });
            }
            return result;
        }

        [Authorize(CasualManagerPermissions.Statisic.Default)]
        public async Task<List<DifferentialRevenueDto>> CalculatingDifferentialRevenue(int month)
        {
            var targets = await _planningRepository.GetListRevenueTargetOfMonths(month);
            var revenues = await _orderRepository.GetListRevenueOfMonthsOfBussiness(month);
            var monthNow = DateTime.Now.Month;
            var result = new List<DifferentialRevenueDto>();
            for (var i = monthNow - month + 1; i <= monthNow; i++)
            {
                string revenue;
                string target;
                if (!revenues.TryGetValue(i.ToString(), out revenue))
                {
                    revenue = "0";
                }
                if (!targets.TryGetValue(i.ToString(), out target))
                {
                    target = "0";
                }
                var rev = Int32.Parse(revenue);
                var tar = Int32.Parse(target);
                result.Add(new DifferentialRevenueDto()
                {
                    Month = i.ToString(),
                    DifferentialRevenue = Math.Abs(rev - tar) * 100 / tar,
                    IsOverTarget = rev >= tar
                });
            }
            return result;
        }

        [Authorize(CasualManagerPermissions.Statisic.Default)]
        public async Task<List<TopProvinceDto>> GetTopProvinceMostUser(int take)
        {
            var customers = await _customerInfoRepository.GetListAsync();
            var workers = await _workerInfoRepository.GetListAsync();
            var customersOderByProvince = customers
                .GroupBy(x => x.ProvinceId)
                .Select(x =>
                {
                    return new
                    {
                        ProvinceId = x.Key,
                        Count = x.Count()
                    };
                })
                .ToDictionary(x => x.ProvinceId, x => x.Count);
            var workersOderByProvince = workers
                .GroupBy(x => x.ProvinceId)
                .Select(x =>
                {
                    return new
                    {
                        ProvinceId = x.Key,
                        Count = x.Count()
                    };
                })
                .ToDictionary(x => x.ProvinceId, x => x.Count);

            var listTopProvince = new List<TopProvinceDto>();
            var provinces = await _addressAppService.GetProvinces();
            foreach(var province in provinces)
            {
                int countCustomer;
                int countWorker;
                if (!customersOderByProvince.TryGetValue(province.Id.ToString(), out countCustomer))
                {
                    countCustomer = 0;
                }
                if (!workersOderByProvince.TryGetValue(province.Id.ToString(), out countWorker))
                {
                    countWorker = 0;
                }
                listTopProvince.Add(new TopProvinceDto() 
                { 
                    CustomerCount = countCustomer,
                    WorkerCount = countWorker,
                    TotalUser = countCustomer + countWorker,
                    ProvinceName = province.Name 
                });
            }
            return listTopProvince.OrderByDescending(x => x.TotalUser).Take(take).ToList();
        }

        [Authorize(CasualManagerPermissions.Statisic.Default)]
        public async Task<List<WorkerInfoDto>> GetTopWorker(int take)
        {
            var result = await _workerInfoRepository.GetListTopWorker(take);
            var listWorkerInfoAllDto = new List<WorkerInfoDto>();
            foreach (var worker in result)
            {
                var workerIdentity = await _identityUserRepository.GetAsync(worker.UserId);
                var workerInfoAllDto = ObjectMapper.Map<WorkerInfo, WorkerInfoDto>(worker);
                workerInfoAllDto.Name = workerIdentity.Name;
                workerInfoAllDto.Email = workerIdentity.Email;
                workerInfoAllDto.Phone = workerIdentity.PhoneNumber;
                workerInfoAllDto.UserName = workerIdentity.UserName;
                listWorkerInfoAllDto.Add(workerInfoAllDto);
            }
            return listWorkerInfoAllDto;
        }

        [Authorize(CasualManagerPermissions.Statisic.Default)]
        public async Task<List<CustomerInfoDto>> GetTopCustomer(int take)
        {
            var result = await _customerInfoRepository.GetListTopCustomer(take);
            var listCustomerInfoAllDto = new List<CustomerInfoDto>();
            foreach (var customer in result)
            {
                var customerIdentity = await _identityUserRepository.GetAsync(customer.UserId);
                var customerInfoAllDto = ObjectMapper.Map<CustomerInfo, CustomerInfoDto>(customer);
                customerInfoAllDto.Name = customerIdentity.Name;
                customerInfoAllDto.Email = customerIdentity.Email;
                customerInfoAllDto.Phone = customerIdentity.PhoneNumber;
                customerInfoAllDto.UserName = customerIdentity.UserName;
                listCustomerInfoAllDto.Add(customerInfoAllDto);
            }
            return listCustomerInfoAllDto;
        }

        [Authorize(CasualManagerPermissions.Statisic.Default)]
        public async Task<List<OrderDto>> GetNearestOrder(int take)
        {
            var result = await _orderRepository.GetListNearest(take);
            var listOrderDto = new List<OrderDto>();
            foreach (var item in result)
            {
                var orderDto = ObjectMapper.Map<Order, OrderDto>(item);
                var identityCustomer = await _identityUserRepository.GetAsync(item.CustomerInfo.UserId);
                var identityWorker = await _identityUserRepository.GetAsync(item.WorkerInfo.UserId);
                orderDto.CustomerName = identityCustomer.Name;
                orderDto.WorkerName = identityWorker.Name;
                listOrderDto.Add(orderDto);
            }
            return listOrderDto;
        }

        [Authorize(CasualManagerPermissions.Statisic.Default)]
        public async Task<List<UserInfoDto>> GetNearestUser(int take)
        {
            var userIdentity = (await _identityUserRepository.GetListAsync()).OrderByDescending(x => x.CreationTime).Take(take);
            var listUserDto = new List<UserInfoDto>();
            foreach(var item in userIdentity)
            {
                var userInfo = new UserInfoDto()
                {
                    Name = item.Name,
                    Date = item.CreationTime.ToString("dd/MM/yyyy")
                };
                if ((await _identityUserManager.GetRolesAsync(item)).Contains(Role.CUSTOMER))
                    userInfo.Role = Role.CUSTOMER;
                else
                    userInfo.Role = Role.WORKER;
                listUserDto.Add(userInfo);
            }
            return listUserDto;
        }

        [Authorize(CasualManagerPermissions.Statisic.Default)]
        public async Task<List<JobInfoDto>> GetNewestJobAdded(int take)
        {
            var result = await _jobInfoRepository.GetNewJobAdded(take);
            var jobInfoDtos = new List<JobInfoDto>();
            foreach(var jobInfo in result)
            {
                var jobInfoDto = ObjectMapper.Map<JobInfo, JobInfoDto>(jobInfo);
                var now = DateTime.Now;
                jobInfoDto.DistanceAdded = jobInfoDto.CreationTime.Year == now.Year ?
                    jobInfoDto.CreationTime.Month == now.Month ?
                    jobInfoDto.CreationTime.Day == now.Day ?
                    jobInfoDto.CreationTime.Hour == now.Hour ?
                    jobInfoDto.CreationTime.Minute == now.Minute ?
                        L["Common:SecondAgo", now.Second - jobInfoDto.CreationTime.Second]
                        : L["Common:MinuteAgo", now.Minute - jobInfoDto.CreationTime.Minute]
                        : L["Common:HourAgo", now.Hour - jobInfoDto.CreationTime.Hour]
                        : L["Common:DayAgo", now.Day - jobInfoDto.CreationTime.Day]
                        : L["Common:MonthAgo", now.Month - jobInfoDto.CreationTime.Month]
                        : L["Common:YearAgo", now.Year - jobInfoDto.CreationTime.Year];
                jobInfoDtos.Add(jobInfoDto);
            }
            return jobInfoDtos;
        }

        [Authorize(CasualManagerPermissions.Statisic.Default)]
        public async Task<int> GetCountOfCustomer()
        {
            return await _customerInfoRepository.CountAsync(x => !x.IsDeleted);
        }

        [Authorize(CasualManagerPermissions.Statisic.Default)]
        public async Task<int> GetCountOfWorker()
        {
            return await _workerInfoRepository.CountAsync(x => !x.IsDeleted);
        }

        [Authorize(CasualManagerPermissions.Statisic.Default)]
        public async Task<int> GetCountOfOrderIsInProcess()
        {
            return await _orderRepository.CountAsync(x => x.Status == OrderStatus.IsInProcess);
        }

        [Authorize(CasualManagerPermissions.Statisic.Default)]
        public async Task<int> GetIncomeInDay()
        {
            return await _orderRepository.GetIncomeInDay();
        }

    }
}
