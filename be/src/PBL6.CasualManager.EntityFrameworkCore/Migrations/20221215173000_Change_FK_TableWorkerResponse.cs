using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace PBL6.CasualManager.Migrations
{
    public partial class Change_FK_TableWorkerResponse : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_WorkerResponse_PostOfDemand_WorkerId",
                table: "WorkerResponse");

            migrationBuilder.CreateIndex(
                name: "IX_WorkerResponse_PostOfDemandId",
                table: "WorkerResponse",
                column: "PostOfDemandId");

            migrationBuilder.AddForeignKey(
                name: "FK_WorkerResponse_PostOfDemand_PostOfDemandId",
                table: "WorkerResponse",
                column: "PostOfDemandId",
                principalTable: "PostOfDemand",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_WorkerResponse_PostOfDemand_PostOfDemandId",
                table: "WorkerResponse");

            migrationBuilder.DropIndex(
                name: "IX_WorkerResponse_PostOfDemandId",
                table: "WorkerResponse");

            migrationBuilder.AddForeignKey(
                name: "FK_WorkerResponse_PostOfDemand_WorkerId",
                table: "WorkerResponse",
                column: "WorkerId",
                principalTable: "PostOfDemand",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
