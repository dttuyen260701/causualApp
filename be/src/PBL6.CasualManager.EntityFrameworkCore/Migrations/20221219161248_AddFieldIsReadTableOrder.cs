using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PBL6.CasualManager.Migrations
{
    public partial class AddFieldIsReadTableOrder : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<bool>(
                name: "IsRead",
                table: "Order",
                type: "bit",
                nullable: false,
                defaultValue: false);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "IsRead",
                table: "Order");
        }
    }
}
