using Microsoft.AspNetCore.Mvc.TagHelpers.Cache;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.Oders;
using PBL6.CasualManager.Statisics;
using PBL6.CasualManager.WorkerInfos;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace PBL6.CasualManager.Web.Pages;

public class IndexModel : CasualManagerPageModel
{
    private const int TAKE = 6;

    private readonly IStatisicAppService _statisicAppService;

    public List<WorkerInfoDto> ListTopWorker { get; set; }
    public List<CustomerInfoDto> ListTopCustomer { get; set; }
    public List<OrderDto> ListNearestOrder { get; set; }
    public List<UserInfoDto> ListNearestUser { get; set; }
    public List<DifferentialRevenueDto> ListDifferentialRevenue { get; set; }
    public List<JobInfoDto> ListNewestJobInfoAdded { get; set; }
    public int CountOfCustomer { get; set; }
    public int CountOfWorker { get; set; }
    public int CountOfOrderIsInProcess { get; set; }
    public int IncomeInDay { get; set; }

    public IndexModel(IStatisicAppService statisicAppService)
    {
        _statisicAppService = statisicAppService;
    }

    public virtual async Task OnGetAsync()
    {
        ListTopWorker = await _statisicAppService.GetTopWorker(TAKE);
        ListTopCustomer = await _statisicAppService.GetTopCustomer(TAKE);
        ListNearestOrder = await _statisicAppService.GetNearestOrder(TAKE);
        ListNearestUser = await _statisicAppService.GetNearestUser(TAKE);
        ListDifferentialRevenue = await _statisicAppService.CalculatingDifferentialRevenue(TAKE);
        ListNewestJobInfoAdded = await _statisicAppService.GetNewestJobAdded(TAKE);
        CountOfCustomer = await _statisicAppService.GetCountOfCustomer();
        CountOfWorker = await _statisicAppService.GetCountOfWorker();
        CountOfOrderIsInProcess = await _statisicAppService.GetCountOfOrderIsInProcess();
        IncomeInDay = await _statisicAppService.GetIncomeInDay();
    }
}
