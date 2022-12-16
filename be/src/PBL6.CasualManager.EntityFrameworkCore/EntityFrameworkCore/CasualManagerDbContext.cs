using Microsoft.EntityFrameworkCore;
using PBL6.CasualManager.CustomerInfos;
using PBL6.CasualManager.Districts;
using PBL6.CasualManager.JobInfoOfWorkers;
using PBL6.CasualManager.JobInfos;
using PBL6.CasualManager.Orders;
using PBL6.CasualManager.PostOfDemands;
using PBL6.CasualManager.PrieceDetails;
using PBL6.CasualManager.Provinces;
using PBL6.CasualManager.RateOfWorkers;
using PBL6.CasualManager.TypeOfJobs;
using PBL6.CasualManager.Wards;
using PBL6.CasualManager.WorkerInfos;
using PBL6.CasualManager.WorkerResponses;
using Volo.Abp.AuditLogging.EntityFrameworkCore;
using Volo.Abp.BackgroundJobs.EntityFrameworkCore;
using Volo.Abp.Data;
using Volo.Abp.DependencyInjection;
using Volo.Abp.EntityFrameworkCore;
using Volo.Abp.EntityFrameworkCore.Modeling;
using Volo.Abp.FeatureManagement.EntityFrameworkCore;
using Volo.Abp.Identity;
using Volo.Abp.Identity.EntityFrameworkCore;
using Volo.Abp.OpenIddict.EntityFrameworkCore;
using Volo.Abp.PermissionManagement.EntityFrameworkCore;
using Volo.Abp.SettingManagement.EntityFrameworkCore;
using Volo.Abp.TenantManagement;
using Volo.Abp.TenantManagement.EntityFrameworkCore;

namespace PBL6.CasualManager.EntityFrameworkCore;

[ReplaceDbContext(typeof(IIdentityDbContext))]
[ReplaceDbContext(typeof(ITenantManagementDbContext))]
[ConnectionStringName("Default")]
public class CasualManagerDbContext :
    AbpDbContext<CasualManagerDbContext>,
    IIdentityDbContext,
    ITenantManagementDbContext
{
    /* Add DbSet properties for your Aggregate Roots / Entities here. */

    #region Entities from the modules

    /* Notice: We only implemented IIdentityDbContext and ITenantManagementDbContext
     * and replaced them for this DbContext. This allows you to perform JOIN
     * queries for the entities of these modules over the repositories easily. You
     * typically don't need that for other modules. But, if you need, you can
     * implement the DbContext interface of the needed module and use ReplaceDbContext
     * attribute just like IIdentityDbContext and ITenantManagementDbContext.
     *
     * More info: Replacing a DbContext of a module ensures that the related module
     * uses this DbContext on runtime. Otherwise, it will use its own DbContext class.
     */

    //Identity
    public DbSet<IdentityUser> Users { get; set; }
    public DbSet<IdentityRole> Roles { get; set; }
    public DbSet<IdentityClaimType> ClaimTypes { get; set; }
    public DbSet<OrganizationUnit> OrganizationUnits { get; set; }
    public DbSet<IdentitySecurityLog> SecurityLogs { get; set; }
    public DbSet<IdentityLinkUser> LinkUsers { get; set; }

    // Tenant Management
    public DbSet<Tenant> Tenants { get; set; }
    public DbSet<TenantConnectionString> TenantConnectionStrings { get; set; }

    #endregion

    #region Entities

    public DbSet<CustomerInfo> CustomerInfos { get; set; }

    public DbSet<WorkerInfo> WorkerInfos { get; set; }

    public DbSet<JobInfoOfWorker> JobInfoOfWorkers { get; set; }

    public DbSet<JobInfo> JobInfos { get; set; }

    public DbSet<TypeOfJob> TypeOfJobs { get; set; }

    public DbSet<Order> Orders { get; set; }

    public DbSet<PrieceDetail> PrieceDetails { get; set; }

    public DbSet<PostOfDemand> PostOfDemands { get; set; }

    public DbSet<WorkerResponse> WorkerResponses { get; set; }

    public DbSet<RateOfWorker> RateOfWorkers { get; set; }

    public DbSet<Province> Provinces { get; set; }

    public DbSet<District> Districts { get; set; }

    public DbSet<Ward> Wards { get; set; }

    #endregion

    public CasualManagerDbContext(DbContextOptions<CasualManagerDbContext> options)
        : base(options)
    {

    }

    protected override void OnModelCreating(ModelBuilder builder)
    {
        base.OnModelCreating(builder);

        /* Include modules to your migration db context */

        builder.ConfigurePermissionManagement();
        builder.ConfigureSettingManagement();
        builder.ConfigureBackgroundJobs();
        builder.ConfigureAuditLogging();
        builder.ConfigureIdentity();
        builder.ConfigureOpenIddict();
        builder.ConfigureFeatureManagement();
        builder.ConfigureTenantManagement();

        /* Configure your own tables/entities inside here */

        //builder.Entity<YourEntity>(b =>
        //{
        //    b.ToTable(CasualManagerConsts.DbTablePrefix + "YourEntities", CasualManagerConsts.DbSchema);
        //    b.ConfigureByConvention(); //auto configure for the base class props
        //    //...
        //});

        builder.Entity<CustomerInfo>(b =>
        {
            b.ToTable(CasualManagerConsts.DbTablePrefix + "CustomerInfo", CasualManagerConsts.DbSchema);
            b.ConfigureByConvention();
        });

        builder.Entity<WorkerInfo>(b =>
        {
            b.ToTable(CasualManagerConsts.DbTablePrefix + "WorkerInfo", CasualManagerConsts.DbSchema);
            b.ConfigureByConvention();
        });

        builder.Entity<JobInfoOfWorker>(b =>
        {
            b.ToTable(CasualManagerConsts.DbTablePrefix + "JobInfoOfWorker", CasualManagerConsts.DbSchema);
            b.HasOne(s => s.WorkerInfo).WithMany(p => p.JobInfoOfWorkers).HasForeignKey(s => s.WorkerId).IsRequired();
            b.HasOne(s => s.JobInfo).WithMany(p => p.JobInfoOfWorkers).HasForeignKey(s => s.JobInfoId).IsRequired();
            b.ConfigureByConvention();
        });

        builder.Entity<JobInfo>(b =>
        {
            b.ToTable(CasualManagerConsts.DbTablePrefix + "JobInfo", CasualManagerConsts.DbSchema);
            b.HasOne(s => s.TypeOfJob).WithMany(p => p.JobInfos).HasForeignKey(s => s.TypeOfJobId).IsRequired();
            b.ConfigureByConvention();
        });

        builder.Entity<TypeOfJob>(b =>
        {
            b.ToTable(CasualManagerConsts.DbTablePrefix + "TypeOfJob", CasualManagerConsts.DbSchema);
            b.ConfigureByConvention();
        });

        builder.Entity<Order>(b =>
        {
            b.ToTable(CasualManagerConsts.DbTablePrefix + "Order", CasualManagerConsts.DbSchema);
            b.HasOne(s => s.CustomerInfo).WithMany(p => p.Orders).HasForeignKey(s => s.CustomerId).IsRequired();
            b.HasOne(s => s.WorkerInfo).WithMany(p => p.Orders).HasForeignKey(s => s.WorkerId).IsRequired();
            b.HasOne(s => s.JobInfo).WithMany(p => p.Orders).HasForeignKey(s => s.JobInfoId).IsRequired();
            b.HasOne(s => s.PrieceDetail).WithOne(p => p.Order).IsRequired();
            b.ConfigureByConvention();
        });

        builder.Entity<PrieceDetail>(b =>
        {
            b.ToTable(CasualManagerConsts.DbTablePrefix + "PrieceDetail", CasualManagerConsts.DbSchema);
            b.ConfigureByConvention();
        });

        builder.Entity<PostOfDemand>(b =>
        {
            b.ToTable(CasualManagerConsts.DbTablePrefix + "PostOfDemand", CasualManagerConsts.DbSchema);
            b.HasOne(s => s.CustomerInfo).WithMany(p => p.PostOfDemands).HasForeignKey(s => s.CustomerId).IsRequired();
            b.HasOne(s => s.JobInfo).WithMany(p => p.PostOfDemands).HasForeignKey(s => s.JobInfoId).IsRequired();
            b.ConfigureByConvention();
        });

        builder.Entity<WorkerResponse>(b =>
        {
            b.ToTable(CasualManagerConsts.DbTablePrefix + "WorkerResponse", CasualManagerConsts.DbSchema);
            b.HasOne(s => s.WorkerInfo).WithMany(p => p.WorkerResponses).HasForeignKey(s => s.WorkerId).IsRequired();
            b.HasOne(s => s.PostOfDemand).WithMany(p => p.WorkerResponses).HasForeignKey(s => s.PostOfDemandId).IsRequired();
            b.ConfigureByConvention();
        });

        builder.Entity<RateOfWorker>(b =>
        {
            b.ToTable(CasualManagerConsts.DbTablePrefix + "RateOfWorker", CasualManagerConsts.DbSchema);
            b.HasOne(s => s.WorkerInfo).WithMany(p => p.RateOfWorkers).HasForeignKey(s => s.WorkerId).IsRequired();
            b.HasOne(s => s.CustomerInfo).WithMany(p => p.RateOfWorkers).HasForeignKey(s => s.CustomerId).IsRequired();
            b.ConfigureByConvention();
        });

        builder.Entity<Province>(b =>
        {
            b.ToTable(CasualManagerConsts.DbTablePrefix + "Province", CasualManagerConsts.DbSchema);
            b.ConfigureByConvention();
        });

        builder.Entity<District>(b =>
        {
            b.ToTable(CasualManagerConsts.DbTablePrefix + "District", CasualManagerConsts.DbSchema);
            b.HasOne(s => s.Province).WithMany(p => p.Districts).HasForeignKey(s => s.ProvinceId).IsRequired();
            b.ConfigureByConvention();
        });

        builder.Entity<Ward>(b =>
        {
            b.ToTable(CasualManagerConsts.DbTablePrefix + "Ward", CasualManagerConsts.DbSchema);
            b.HasOne(s => s.District).WithMany(p => p.Wards).HasForeignKey(s => s.DistrictId).IsRequired();
            b.ConfigureByConvention();
        });
    }
}
