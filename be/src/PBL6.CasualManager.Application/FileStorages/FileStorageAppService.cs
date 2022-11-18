using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using System.IO;
using System.Threading.Tasks;
using System;

namespace PBL6.CasualManager.FileStorages
{
    public class FileStorageAppService : CasualManagerAppService, IFileStorageAppService
    {
        private readonly IHostingEnvironment _hostingEnvironment;

        public FileStorageAppService(IHostingEnvironment hostingEnvironment)
        {
            _hostingEnvironment = hostingEnvironment;
        }

        public async Task<bool> SaveImageAsync(IFormFile image, string path)
        {
            if (image == null)
            {
                return false;
            }
            try
            {
                using (FileStream fileStream = System.IO.File.Create($"{_hostingEnvironment.WebRootPath}/{path}"))
                {
                    await image.CopyToAsync(fileStream);
                    await fileStream.FlushAsync();
                }
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public bool DeleteImageAsync(string path)
        {
            if (path == null)
            {
                return true;
            }
            try
            {
                var fullPath =  $"{_hostingEnvironment.WebRootPath}/{path}";
                System.IO.File.Delete(fullPath);
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

    }
}
