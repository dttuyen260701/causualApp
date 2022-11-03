using System;
using System.Threading.Tasks;

namespace PBL6.CasualManager.CustomerInfos
{
    public class CustomerInfoAppService : CasualManagerAppService, ICustomerInfoAppService
    {
        private readonly ICustomerInfoRepository _customerInfoRepository;

        public CustomerInfoAppService(ICustomerInfoRepository customerInfoRepository)
        {
            _customerInfoRepository = customerInfoRepository;
        }

        public async Task<bool> AddAsync(CustomerInfoDto customerInfosDto)
        {
            var customerInfo = ObjectMapper.Map<CustomerInfoDto, CustomerInfo>(customerInfosDto);
            try
            {
                await _customerInfoRepository.InsertAsync(customerInfo, true);
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }
    }
}