using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Routing;
using PBL6.CasualManager.ApiResults;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Enum;
using PBL6.CasualManager.WorkerInfos;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Domain.Repositories;
using Volo.Abp.Identity;

namespace PBL6.CasualManager.RateOfWorkers
{
    public class RateOfWorkerAppService : CasualManagerAppService, IRateOfWorkerAppService
    {
        private readonly IRateOfWorkerRepository _rateOfWorkerRepository;
        private readonly IWorkerInfoRepository _workerInfoRepository;
        private readonly ICustomerInfoRepository _customerInfoRepository;
        private readonly IIdentityUserRepository _identityUserRepository;
        public RateOfWorkerAppService(IRateOfWorkerRepository rateOfWorkerRepository, IWorkerInfoRepository workerInfoRepository,
            ICustomerInfoRepository customerInfoRepository, IIdentityUserRepository identityUserRepository)
        {
            _rateOfWorkerRepository = rateOfWorkerRepository;
            _workerInfoRepository = workerInfoRepository;
            _customerInfoRepository = customerInfoRepository;
            _identityUserRepository = identityUserRepository;
        }

        [HttpGet]
        [Route("api/app/rate-of-worker/{workerId}/detail")]
        public async Task<ApiResult<RateOfWorkerResponse>> GetRateOfWorkerAsync(Guid workerId)//for web
        {
            try
            {
                var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(workerId);
                if (workerInfo == null)
                {
                    return new ApiErrorResult<RateOfWorkerResponse>(message: "Không tìm thấy người dùng.");
                }
                var listRateOfWorker = await _rateOfWorkerRepository.GetListAsync(x => x.WorkerId == workerInfo.Id);
                var result = CreateRateOfWorkerDetailFrom((from rateOfWorker in listRateOfWorker select rateOfWorker).ToList());
                return new ApiSuccessResult<RateOfWorkerResponse>(resultObj: result);
            }
            catch (Exception)
            {
                return new ApiErrorResult<RateOfWorkerResponse>(message: "Lỗi");
            }
        }
        [HttpGet]
        [Route("api/app/rate-of-worker/{workerId}/list-rate/detail")]
        public async Task<ApiResult<List<RateOfWorkerDetailResponse>>> GetListRateOfWorker(Guid workerId)
        {
            try
            {
                var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(workerId);
                if (workerInfo == null)
                {
                    return new ApiErrorResult<List<RateOfWorkerDetailResponse>>(message: "Không tìm thấy thông tin người dùng!");
                }
                var listCustomerInfo = await _customerInfoRepository.GetListAsync();
                var listIdentityInfo = await _identityUserRepository.GetListAsync();
                var listRateOfWorker = await _rateOfWorkerRepository.GetListAsync(x => x.WorkerId == workerInfo.Id);
                var results = (from rateOfWorker in listRateOfWorker
                               join cus in listCustomerInfo on rateOfWorker.CustomerId equals cus.Id
                               join userIden in listIdentityInfo on cus.UserId equals userIden.Id
                               orderby rateOfWorker.CreationTime descending
                               select new RateOfWorkerDetailResponse()
                               {
                                   RateAverage = CalculateAverageRate(rateOfWorker),
                                   Comment = rateOfWorker.Comment,
                                   CustomerImage = string.IsNullOrEmpty(cus.Avatar) ? Constants.ImageDefaultCustomer : cus.Avatar,
                                   CustomerName = userIden.Name,
                                   CreationTime = rateOfWorker.CreationTime.ToString("dd-MM-yyy HH:mm")
                               }).ToList();
                return new ApiSuccessResult<List<RateOfWorkerDetailResponse>>(resultObj: results);
            }
            catch (Exception)
            {
                return new ApiErrorResult<List<RateOfWorkerDetailResponse>>(message: "Có lỗi trong quá trình lấy dữ liệu!");
            }
        }

        [HttpPost]
        [Route("api/app/rate-of-worker/{customerId}/create-rate")]
        public async Task<ApiResult<CreateRateOfWorkerResponse>> CreateRateOfWorker(Guid customerId, CreateRateOfWorkerRequest request)
        {
            try
            {
                var customerInfo = await _customerInfoRepository.GetEntityCustomerInfoHaveUserId(customerId);
                var workerInfo = await _workerInfoRepository.GetEntityWorkerInfoHaveUserId(request.WorkerId);
                var customerIdentity = await _identityUserRepository.GetAsync(customerId);
                if (workerInfo == null)
                {
                    return new ApiErrorResult<CreateRateOfWorkerResponse>(message: "Không tìm thấy thông tin thợ!");
                }
                var checkExistRate = await _rateOfWorkerRepository.FirstOrDefaultAsync(x => x.OrderId == request.OrderId);
                if (checkExistRate != null)
                {
                    return new ApiErrorResult<CreateRateOfWorkerResponse>(message: "Bạn đã đánh giá đơn này!");
                }
                var workerIdentity = await _identityUserRepository.GetAsync(workerInfo.UserId);
                var rateOfWorker = new RateOfWorker()
                {
                    OrderId = request.OrderId,
                    Comment = request.Comment,
                    PleasureRate = request.PleasureRate,
                    AttitudeRate = request.AttitudeRate,
                    SkillRate = request.SkillRate,
                    WorkerId = workerInfo.Id,
                    CustomerId = customerInfo.Id,
                };
                var result = await _rateOfWorkerRepository.InsertAsync(rateOfWorker, true);
                if (result == null)
                {
                    return new ApiErrorResult<CreateRateOfWorkerResponse>(message: "Tạo không thành công!");
                }
                var listRateOfWorker = await _rateOfWorkerRepository.GetListAsync(x => x.WorkerId == workerInfo.Id);
                var rateAverage = CreateRateOfWorkerDetailFrom(listRateOfWorker);
                return new ApiSuccessResult<CreateRateOfWorkerResponse>(resultObj: new CreateRateOfWorkerResponse()
                {
                    Id = result.Id,
                    SkillRate = result.SkillRate,
                    AttitudeRate = result.AttitudeRate,
                    PleasureRate = result.PleasureRate,
                    Comment = result.Comment,
                    CreationTime = result.CreationTime.ToString("dd-MM-yyyy"),
                    CustomerId = customerIdentity.Id,
                    WorkerId = workerIdentity.Id,
                    CustomerName = customerIdentity.Name,
                    WorkerName = workerIdentity.Name,
                    OrderId = result.OrderId,
                    AttitudeRateAverage = rateAverage.AttitudeRateAverage,
                    PleasureRateAverage = rateAverage.PleasureRateAverage,
                    SkillRateAverage = rateAverage.SkillRateAverage,
                    RateAverage = rateAverage.RateAverage
                });
            }
            catch (Exception)
            {
                return new ApiErrorResult<CreateRateOfWorkerResponse>(message: "Đã xảy ra lỗi trong quá trình tạo!");
            }
        }

        [HttpGet]
        [Route("api/app/rate-of-worker/rate-worker-of-order/{orderId}")]
        public async Task<ApiResult<RateOfWorkerDetailResponse>> GetRateOfWorkerByOrderId(Guid orderId)
        {
            try
            {
                var rateOfWorkerInOrder = await _rateOfWorkerRepository.FirstOrDefaultAsync(x => x.OrderId == orderId);
                if (rateOfWorkerInOrder == null)
                {
                    return new ApiErrorResult<RateOfWorkerDetailResponse>(message: "Không tồn tại bài đánh giá đơn này!");
                }
                var workerInfo = await _workerInfoRepository.GetAsync(rateOfWorkerInOrder.WorkerId);
                var workerIdentityInfo = await _identityUserRepository.GetAsync(workerInfo.UserId);
                var listRateOfWorker = await _rateOfWorkerRepository.GetListAsync(x => x.WorkerId == workerInfo.Id);
                var rateAverage = CreateRateOfWorkerDetailFrom(listRateOfWorker);
                var rateOfWorkerDetailResponse = new RateOfWorkerDetailResponse()
                {
                    OrderId = rateOfWorkerInOrder.OrderId,
                    Comment = rateOfWorkerInOrder.Comment,
                    WorkerImage = string.IsNullOrEmpty(workerInfo.Avatar) ? Constants.ImageDefaultWorker : workerInfo.Avatar,
                    WorkerName = workerIdentityInfo.Name,
                    RateAverage = rateAverage.RateAverage,
                    AttitudeRate = rateOfWorkerInOrder.AttitudeRate,
                    PleasureRate = rateOfWorkerInOrder.PleasureRate,
                    SkillRate = rateOfWorkerInOrder.SkillRate
                };
                return new ApiSuccessResult<RateOfWorkerDetailResponse>(resultObj: rateOfWorkerDetailResponse);
            }
            catch (Exception)
            {
                return new ApiErrorResult<RateOfWorkerDetailResponse>(message: "Có lỗi trong quá trình lấy dữ liệu!");
            }
        }

        private RateOfWorkerResponse CreateRateOfWorkerDetailFrom(List<RateOfWorker> rateOfWorkers)
        {
            int len = rateOfWorkers.Count();
            if (rateOfWorkers is null || len == 0)
            {
                return new RateOfWorkerResponse()
                {
                    AttitudeRateAverage = 0,
                    PleasureRateAverage = 0,
                    RateAverage = 0,
                    SkillRateAverage = 0
                };
            }
            int rateAttitude = 0;
            int rateSkill = 0;
            int ratePleasure = 0;
            foreach (var rateOfWorker in rateOfWorkers)
            {
                rateAttitude += rateOfWorker.AttitudeRate;
                rateSkill += rateOfWorker.SkillRate;
                ratePleasure += rateOfWorker.PleasureRate;
            }
            return new RateOfWorkerResponse()
            {
                AttitudeRateAverage = (float)rateAttitude / len,
                PleasureRateAverage = (float)ratePleasure / len,
                SkillRateAverage = (float)rateSkill / len,
                RateAverage = (rateAttitude + ratePleasure + rateSkill) / (3 * len)
            };
        }
        private float CalculateAverageRate(RateOfWorker rateOfWorker)
        {
            return (float)(rateOfWorker.AttitudeRate + rateOfWorker.SkillRate + rateOfWorker.PleasureRate) / 3;
        }
    }
}
