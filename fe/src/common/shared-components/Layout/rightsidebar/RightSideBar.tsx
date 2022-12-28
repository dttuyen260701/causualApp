import { Heading, VStack, Text } from "@chakra-ui/react";
import React from "react";
import { useSelector } from "react-redux";
import ReactApexChart from "react-apexcharts";
import { FaUsers } from "react-icons/fa";
import { GiTakeMyMoney } from "react-icons/gi";
import { GoListOrdered } from "react-icons/go";

import {
	useGetJobTotalInfoOfWorkerQuery,
	useGetRateOfWorkerQuery
} from "../../../../app/api/jobManageWorker/jobStatisticApi";
import { getCurrentWorker } from "../../../../app/reducer/loginAuth/loginAuthSlice";

import { InfoCard } from "./InfoCard";

interface IRightSideBar {
	width: string | number;
}

export const RightSideBar: React.FC<IRightSideBar> = ({ width }) => {
	const resultObj = useSelector(getCurrentWorker);
	const { data: dataJobTotal } = useGetJobTotalInfoOfWorkerQuery(resultObj.id);
	const { data: dataRate } = useGetRateOfWorkerQuery(resultObj.id);
	const rateSeries =
		[
			Number(dataRate?.resultObj.attitudeRateAverage),
			Number(dataRate?.resultObj.pleasureRateAverage),
			Number(dataRate?.resultObj.skillRateAverage)
		] ?? [];

	const options: ApexCharts.ApexOptions = {
		labels: ["Thái độ", "Hài lòng", "Kỹ năng"],
		colors: ["#c75422", "#f2e9c5", "#f9d475"],
		chart: {
			type: "donut",
			width: "100%"
		},
		dataLabels: {
			enabled: true,
			style: {
				colors: ["#000000"]
			},
			dropShadow: {
				enabled: false
			}
		},
		plotOptions: {
			pie: {
				startAngle: -90,
				endAngle: 90,
				offsetY: 10
			}
		},
		grid: {
			padding: {
				bottom: -80
			}
		},
		responsive: [
			{
				breakpoint: 480,
				options: {
					chart: {
						width: 200
					},
					legend: {
						position: "bottom"
					}
				}
			}
		],
		legend: {
			position: "bottom",
			fontSize: "15px",
			horizontalAlign: "center",
			onItemHover: {
				highlightDataSeries: true
			},
			labels: {
				colors: "#000000"
			}
		}
	};
	const series = [...rateSeries];

	return (
		<VStack
			spacing={17}
			width={width}
			bgColor="#f3f0ec"
			padding={"30px 10px"}
			height="100%"
			borderRadius={"15px"}
			boxShadow="5px 5px 20px 15px rgb(201, 197, 191, 0.6)"
		>
			<VStack width={"100%"}>
				<Heading textAlign={"left"} color={"#f9d475"} width="100%" fontWeight={"600"} fontSize="25px">
					Thống kê
				</Heading>
				<InfoCard
					title="Tổng doanh thu"
					detail={`${String(dataJobTotal?.resultObj.totalRevenue)} VND` ?? "0 VND"}
					icon={<GiTakeMyMoney color="white" />}
				/>
				<InfoCard
					title="Tổng đơn hàng"
					detail={String(dataJobTotal?.resultObj.totalOrder) ?? "0"}
					icon={<FaUsers color="white" />}
				/>
				<InfoCard
					title="Tổng công việc"
					detail={String(dataJobTotal?.resultObj.totalJob) ?? "0"}
					icon={<GoListOrdered color="white" />}
				/>
			</VStack>
			<VStack width={"100%"} height="100%" spacing={7} alignItems="center">
				<Text
					width={"100%"}
					textAlign="left"
					padding="5px 5px"
					borderRadius={"15px"}
					fontWeight="600"
					color={"#f9d475"}
					fontSize="20px"
				>
					Đánh giá tổng quan
				</Text>
				{rateSeries.every(item => item === 0) ? (
					<Text color={"#a6a09a"}>Không có đánh giá dành cho bạn</Text>
				) : (
					<ReactApexChart type="donut" width={"100%"} height={"100%"} options={options} series={series} />
				)}
			</VStack>
		</VStack>
	);
};
