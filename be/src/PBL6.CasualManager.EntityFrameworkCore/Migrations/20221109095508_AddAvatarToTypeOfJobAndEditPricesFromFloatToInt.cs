using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PBL6.CasualManager.Migrations
{
    public partial class AddAvatarToTypeOfJobAndEditPricesFromFloatToInt : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<string>(
                name: "Avatar",
                table: "TypeOfJob",
                type: "nvarchar(max)",
                nullable: true);

            migrationBuilder.AlterColumn<int>(
                name: "Prices",
                table: "JobInfo",
                type: "int",
                nullable: false,
                oldClrType: typeof(float),
                oldType: "real");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "Avatar",
                table: "TypeOfJob");

            migrationBuilder.AlterColumn<float>(
                name: "Prices",
                table: "JobInfo",
                type: "real",
                nullable: false,
                oldClrType: typeof(int),
                oldType: "int");
        }
    }
}
