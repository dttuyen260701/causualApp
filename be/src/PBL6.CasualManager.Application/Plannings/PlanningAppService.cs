using Microsoft.AspNetCore.Authorization;
using PBL6.CasualManager.Permissions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Application.Services;

namespace PBL6.CasualManager.Plannings
{
    [Authorize(CasualManagerPermissions.Planning.Default)]
    public class PlanningAppService :
        CrudAppService<
            Planning,
            PlanningDto,
            Guid,
            PlanningConditionSearchDto,
            PlanningCreateUpdateDto>, 
        IPlanningAppService
    {
        private readonly IPlanningRepository _planningRepository;

        public PlanningAppService(IPlanningRepository planningRepository) : base(planningRepository)
        {
            _planningRepository = planningRepository;
        }

        public async Task<Dictionary<string, string>> GetListRevenueTargetOfMonthsAsync(int month = 1, int year = 0)
        {
            return await _planningRepository.GetListRevenueTargetOfMonths(month, year);
        }

        public async Task<PagedResultDto<PlanningDto>> GetListRevenueTargetByYearAsync(PlanningConditionSearchDto condition)
        {
            var result = await _planningRepository.GetListRevenueTargetByYearAsync(
                condition.SkipCount,
                condition.MaxResultCount,
                condition.Sorting,
                condition.FilterYear);
            return ObjectMapper.Map<PagedResultDto<Planning>, PagedResultDto<PlanningDto>>(result);
        }

        public async Task<List<PlanningDto>> GetListTargetByYearAsync(int year)
        {
            var result = (await _planningRepository.GetListAsync()).Where(x => x.Year == year).ToList();
            return ObjectMapper.Map<List<Planning>, List<PlanningDto>>(result);
        }

        public async Task<List<int>> GetListYearHaveData()
        {
            var result = (await _planningRepository.GetListAsync()).Select(x => x.Year).Distinct();
            return result.ToList();
        }

        [Authorize(CasualManagerPermissions.Planning.Create)]
        [Authorize(CasualManagerPermissions.Planning.Update)]
        public async Task CreateUpdateRevenueTargetInYear(PlanningCreateUpdateDto createUpdateData)
        {
            if (await _planningRepository.IsExistTargetInYear(createUpdateData.Year))
            {
                var listTargetInYear = (await _planningRepository.GetListAsync()).Where(x => x.Year == createUpdateData.Year).ToList();
                foreach(var item in listTargetInYear)
                {
                    switch (item.Month)
                    {
                        case 1:
                            item.UpdateTarget(createUpdateData.TargetInJan);
                            break;
                        case 2:
                            item.UpdateTarget(createUpdateData.TargetInFeb);
                            break;
                        case 3:
                            item.UpdateTarget(createUpdateData.TargetInMarch);
                            break;
                        case 4:
                            item.UpdateTarget(createUpdateData.TargetInApril);
                            break;
                        case 5:
                            item.UpdateTarget(createUpdateData.TargetInMay);
                            break;
                        case 6:
                            item.UpdateTarget(createUpdateData.TargetInJune);
                            break;
                        case 7:
                            item.UpdateTarget(createUpdateData.TargetInJully);
                            break;
                        case 8:
                            item.UpdateTarget(createUpdateData.TargetInAugust);
                            break;
                        case 9:
                            item.UpdateTarget(createUpdateData.TargetInSep);
                            break;
                        case 10:
                            item.UpdateTarget(createUpdateData.TargetInOct);
                            break;
                        case 11:
                            item.UpdateTarget(createUpdateData.TargetInNov);
                            break;
                        case 12:
                            item.UpdateTarget(createUpdateData.TargetInDec);
                            break;
                        default:
                            break;
                    }
                }
                await _planningRepository.UpdateManyAsync(listTargetInYear);
            }
            else
            {
                var createData = new List<Planning>()
                {
                    new Planning() { Year = createUpdateData.Year, Month = 1, RevenueTarget = createUpdateData.TargetInJan },
                    new Planning() { Year = createUpdateData.Year, Month = 2, RevenueTarget = createUpdateData.TargetInFeb },
                    new Planning() { Year = createUpdateData.Year, Month = 3, RevenueTarget = createUpdateData.TargetInMarch },
                    new Planning() { Year = createUpdateData.Year, Month = 4, RevenueTarget = createUpdateData.TargetInApril },
                    new Planning() { Year = createUpdateData.Year, Month = 5, RevenueTarget = createUpdateData.TargetInMay },
                    new Planning() { Year = createUpdateData.Year, Month = 6, RevenueTarget = createUpdateData.TargetInJune },
                    new Planning() { Year = createUpdateData.Year, Month = 7, RevenueTarget = createUpdateData.TargetInJully },
                    new Planning() { Year = createUpdateData.Year, Month = 8, RevenueTarget = createUpdateData.TargetInAugust },
                    new Planning() { Year = createUpdateData.Year, Month = 9, RevenueTarget = createUpdateData.TargetInSep },
                    new Planning() { Year = createUpdateData.Year, Month = 10, RevenueTarget = createUpdateData.TargetInOct },
                    new Planning() { Year = createUpdateData.Year, Month = 11, RevenueTarget = createUpdateData.TargetInNov },
                    new Planning() { Year = createUpdateData.Year, Month = 12, RevenueTarget = createUpdateData.TargetInDec },
                };
                await _planningRepository.InsertManyAsync(createData);
            }
        }
    }
}
