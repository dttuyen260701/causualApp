using PBL6.CasualManager.Wards;
using PBL6.CasualManager.Districts;
using PBL6.CasualManager.Provinces;
using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;
using Volo.Abp.Domain.Services;

namespace PBL6.CasualManager.ProvinceApi
{
    public class ProvinceApiManager : DomainService
    {
        private const string HOST_URL = "https://provinces.open-api.vn/api/?depth=3";

        private readonly HttpClient client;

        public ProvinceApiManager()
        {
            client = new HttpClient();
        }

        public async Task<List<Province>> GetInfoFromApi()
        {
            SetHeader();
            try
            {
                HttpResponseMessage response = await client.GetAsync("");
                response.EnsureSuccessStatusCode();
                var provinces = await response.Content.ReadFromJsonAsync<List<Province>>();
                return provinces;
            }
            catch (Exception)
            {
                throw new Exception();
            }
        }

        public void SetHeader()
        {
            client.BaseAddress = new Uri(HOST_URL); 
            client.DefaultRequestHeaders.Clear();
            client.DefaultRequestHeaders.Add("accept", "application/json");
        }

        public class InfoResponse
        {
            public List<Province> Provinces { get; set; }
        }
    }
}
