using PBL6.CasualManager.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Volo.Abp.Domain.Repositories.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore;

namespace PBL6.CasualManager.PrieceDetails
{
    public class PrieceDetailRepository : EfCoreRepository<CasualManagerDbContext, PrieceDetail, Guid>, IPrieceDetailRepository
    {
        public PrieceDetailRepository(IDbContextProvider<CasualManagerDbContext> dbContextProvider) : base(dbContextProvider)
        {
        }
    }
}
