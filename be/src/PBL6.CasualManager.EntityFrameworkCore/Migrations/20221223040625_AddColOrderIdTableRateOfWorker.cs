using System;
using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PBL6.CasualManager.Migrations
{
    public partial class AddColOrderIdTableRateOfWorker : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<Guid>(
                name: "OrderId",
                table: "RateOfWorker",
                type: "uniqueidentifier",
                nullable: false,
                defaultValue: new Guid("00000000-0000-0000-0000-000000000000"));
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "OrderId",
                table: "RateOfWorker");
        }
    }
}
