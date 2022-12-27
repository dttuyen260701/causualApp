using PBL6.CasualManager.JobInfos;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Volo.Abp.Application.Dtos;
using Volo.Abp.Identity;

namespace PBL6.CasualManager.PostOfDemands
{
    public class PostOfDemandAppService : CasualManagerAppService, IPostOfDemandAppService
    {
        private readonly IPostOfDemandRepository _postOfDemandRepository;
        private readonly IIdentityUserRepository _identityUserRepository;

        public PostOfDemandAppService(
            IPostOfDemandRepository postOfDemandRepository,
            IIdentityUserRepository identityUserRepository)
        {
            _postOfDemandRepository = postOfDemandRepository;
            _identityUserRepository = identityUserRepository;
        }

        public async Task<PagedResultDto<PostOfDemandDto>> GetListSearchAsync(PostOfDemandConditionSearchDto condition)
        {
            var result = await _postOfDemandRepository.GetListSearchAsync(
                condition.SkipCount,
                condition.MaxResultCount,
                condition.Sorting,
                condition.FilterCustomer);
            var postOfDemandDtos = new List<PostOfDemandDto>();
            foreach (var item in result.Items)
            {
                var now = DateTime.Now;
                var postOfDemandDto = ObjectMapper.Map<PostOfDemand, PostOfDemandDto>(item);
                var identityCustomer = await _identityUserRepository.GetAsync(item.CustomerInfo.UserId);
                postOfDemandDto.CustomerName = identityCustomer.Name;
                postOfDemandDto.DistanceUpLoad = item.CreationTime.Year == now.Year ?
                    item.CreationTime.Month == now.Month ?
                    item.CreationTime.Day == now.Day ?
                    item.CreationTime.Hour == now.Hour ?
                    item.CreationTime.Minute == now.Minute ?
                        L["Common:SecondAgo", now.Second - item.CreationTime.Second]
                        : L["Common:MinuteAgo", now.Minute - item.CreationTime.Minute]
                        : L["Common:HourAgo", now.Hour - item.CreationTime.Hour]
                        : L["Common:DayAgo", now.Day - item.CreationTime.Day]
                        : L["Common:MonthAgo", now.Month - item.CreationTime.Month]
                        : L["Common:YearAgo", now.Year - item.CreationTime.Year];
                postOfDemandDtos.Add(postOfDemandDto);
            }
            return new PagedResultDto<PostOfDemandDto> { Items = postOfDemandDtos, TotalCount = result.TotalCount};
        }

        public async Task DeleteAsync(Guid id)
        {
            await _postOfDemandRepository.DeleteAsync(id);
        }
    }
}
