using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Hosting;
using System.IO;
using System.Threading.Tasks;
using System;

namespace PBL6.CasualManager.FileStorages
{
    public interface IFileStorageAppService
    {
        Task<bool> SaveImageAsync(IFormFile image, string path);

        bool DeleteImageAsync(string path);
    }
}
