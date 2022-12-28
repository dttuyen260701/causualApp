import { Heading, HStack, Table, TableContainer, Tbody, Td, Th, Thead, Tr, VStack, Text } from "@chakra-ui/react";
import React, { useState } from "react";
import { useSelector } from "react-redux";

import { useGetListOrderOfWorkerQuery } from "../../../app/api/jobManageWorker/jobStatisticApi";
import { getCurrentWorker } from "../../../app/reducer/loginAuth/loginAuthSlice";
import { OrderStatus } from "../../../common/constants/constants";
import { Loading } from "../../../common/shared-components/Loading/Loading";
import Pagination from "../../../common/shared-components/pagination/Pagination";

export const LastTransaction: React.FC = () => {
	const resultObj = useSelector(getCurrentWorker);
	const [currentPage, setCurrentPage] = useState(1);
	const { data: dataOrders, isLoading } = useGetListOrderOfWorkerQuery({
		workerId: resultObj.id,
		pageSize: 5,
		pageIndex: currentPage,
		keyword: ""
	});

	return (
		<VStack width={"100%"} spacing={5} height="100%">
			<HStack width={"100%"} justifyContent="flex-start">
				<Heading width={"100%"} textAlign="left" fontWeight={700} fontSize="20px" paddingLeft={"20px"}>
					Những công việc gần đây của bạn
				</Heading>
			</HStack>
			<TableContainer width={"100%"} height="100%">
				<Table variant="mytable">
					<Thead bgColor={"red"}>
						<Tr>
							<Th color={"#f9d475"} bgColor="#f9faf5">
								Tên công việc
							</Th>
							<Th color={"#f9d475"} bgColor="#f9faf5">
								Ngày
							</Th>
							<Th color={"#f9d475"} bgColor="#f9faf5">
								Khách hàng
							</Th>
							<Th color={"#f9d475"} bgColor="#f9faf5">
								Đơn giá
							</Th>
							<Th color={"#f9d475"} bgColor="#f9faf5">
								Trạng thái đơn
							</Th>
						</Tr>
					</Thead>
					{isLoading ? (
						<Loading />
					) : (
						<Tbody height={"100%"} maxH={"58vh"} overflow="scroll">
							{dataOrders?.resultObj.items &&
								dataOrders?.resultObj.items.map((order, index) => (
									<Tr key={order.id} background={index % 2 == 0 ? "#f2e9c5" : ""}>
										<Td>{order.jobInfoName}</Td>
										<Td>{order.creationTime}</Td>
										<Td>{order.customerName}</Td>
										<Td>{`${order.jobPrices} VND`}</Td>
										<Td>
											<Text
												border={`1px solid ${OrderStatus[Number(order.status)].color}`}
												color={OrderStatus[Number(order.status)].color}
												padding="7px"
												textAlign={"center"}
												borderRadius="15px"
											>
												{OrderStatus[Number(order.status)].status}
											</Text>
										</Td>
									</Tr>
								))}
						</Tbody>
					)}
				</Table>
				{dataOrders?.resultObj.items.length === 0 && (
					<Text width={"100%"} textAlign="center" padding={"10px 0px"} paddingTop="15px">
						Không có đơn đặt hàng
					</Text>
				)}
			</TableContainer>
			<Pagination
				currentPage={dataOrders?.resultObj.pageIndex ?? 0}
				totalCount={dataOrders?.resultObj.totalRecords ?? 0}
				pageSize={dataOrders?.resultObj.pageSize ?? 0}
				onPageChange={page => {
					setCurrentPage(page);
				}}
				siblingCount={3}
			/>
		</VStack>
	);
};
