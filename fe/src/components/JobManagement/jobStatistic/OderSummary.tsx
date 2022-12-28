import { Heading, HStack, VStack } from "@chakra-ui/react";
import React from "react";
import ReactApexChart from "react-apexcharts";
import { useSelector } from "react-redux";

import { useGet6MonthJobStaticticQuery } from "../../../app/api/jobManageWorker/jobStatisticApi";
import { getCurrentWorker } from "../../../app/reducer/loginAuth/loginAuthSlice";
import { useGetRightWidth } from "../../../common/hooks/useGetRightWidth";

export const OrderSummary: React.FC = () => {
	const rightWidth = useGetRightWidth();
	const userInfo = useSelector(getCurrentWorker);
	const { data: dataJobStatistic } = useGet6MonthJobStaticticQuery(userInfo.id);

	const sumOfOrder =
		dataJobStatistic?.resultObj.length !== 0
			? dataJobStatistic?.resultObj.reduce((sum, item) => (sum += Number(item.totalOrder)), 0)
			: 0;

	const options: ApexCharts.ApexOptions = {
		colors: ["#393434", "#f1d583"],
		chart: {
			width: "100%",
			height: 550,
			type: "line",
			stacked: false
		},
		dataLabels: {
			enabled: false
		},
		stroke: {
			width: [1, 1, 4]
		},
		title: {
			text: "",
			align: "left",
			offsetX: 110
		},
		xaxis: {
			categories:
				dataJobStatistic?.resultObj.length !== 0
					? dataJobStatistic?.resultObj.map(item => {
							return Number(item.month);
					  })
					: []
		},
		yaxis: [
			{
				axisTicks: {
					show: true
				},
				axisBorder: {
					show: true,
					color: "#393434"
				},
				labels: {
					style: {
						colors: "#393434"
					}
				},
				title: {
					text: "Doanh thu 6 tháng gần đây (VNĐ)",
					style: {
						color: "#393434",
						fontSize: "15px"
					}
				},
				tooltip: {
					enabled: true
				}
			},
			{
				seriesName: "Đơn đặt",
				opposite: true,
				axisTicks: {
					show: true
				},
				axisBorder: {
					show: true,
					color: "#f1d583"
				},
				labels: {
					style: {
						colors: "#f1d583"
					}
				},
				title: {
					text: "Số lượng đơn đặt (6 tháng gần đây)",
					style: {
						color: "#f1d583",
						fontSize: "15px"
					}
				}
			}
		],
		tooltip: {
			fixed: {
				enabled: true,
				position: "topBottom", // topRight, topLeft, bottomRight, bottomLeft
				offsetY: 30,
				offsetX: 60
			}
		},
		legend: {
			position: "top",
			fontSize: "15px",
			horizontalAlign: "center",
			onItemHover: {
				highlightDataSeries: true
			}
		}
	};
	const series: ApexAxisChartSeries = [
		{
			name: "Doanh thu",
			type: "column",
			data:
				dataJobStatistic?.resultObj.map(item => {
					return Number(item.revenue);
				}) ?? []
		},
		{
			name: "Đơn đặt",
			type: "column",
			data:
				dataJobStatistic?.resultObj.map(item => {
					return Number(item.totalOrder);
				}) ?? []
		}
	];
	return (
		<VStack width={"100%"} spacing={3}>
			<Heading
				width={"100%"}
				textAlign="left"
				fontWeight={700}
				fontSize="30px"
				paddingLeft={"20px"}
				paddingTop={"10px"}
				color="#674937"
			>
				Thống kê doanh thu
			</Heading>
			<Heading width={"100%"} textAlign="left" fontWeight={700} fontSize="25px" paddingLeft={"20px"} color="#674937">
				{`Tổng số đơn đặt : ${sumOfOrder}`}
			</Heading>
			<HStack width={"100%"} height="50vh">
				<ReactApexChart width={rightWidth === 1060 ? "710" : "820"} height="100%" options={options} series={series} />
			</HStack>
		</VStack>
	);
};
