import {
	Heading,
	HStack,
	VStack,
	Text,
	Menu,
	MenuButton,
	Box,
	Img,
	MenuList,
	MenuItem,
	useMediaQuery,
	Avatar
} from "@chakra-ui/react";
import React, { useEffect, useState } from "react";
import { AiOutlineInfoCircle } from "react-icons/ai";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

import { getCurrentWorker } from "../../app/reducer/loginAuth/loginAuthSlice";
import { RightSideBar } from "../../common/shared-components/Layout/rightsidebar/RightSideBar";
import { API_URL } from "../../common/utils/config";

import { LastTransaction } from "./jobStatistic/LastTransaction";
import { OrderSummary } from "./jobStatistic/OderSummary";

export const HomePage: React.FC = () => {
	const [showSideBar] = useMediaQuery("(min-width: 1100px)");
	const resultObj = useSelector(getCurrentWorker);
	const navigate = useNavigate();
	const [nameAvatar, setNameAvatar] = useState("");

	useEffect(() => {
		const data = resultObj.name.split(" ");
		data.length > 1 ? setNameAvatar(data[0] + " " + data[data.length - 1]) : setNameAvatar(data[0]);
	}, [resultObj.name]);

	return (
		<HStack w={"100%"} height="100%" justifyContent={"space-between"} paddingBottom="20px" spacing={5}>
			<VStack width={"70%"} alignItems="flex-start" height="100%" spacing="0">
				<HStack w={"100%"} justifyContent={"space-between"}>
					<Heading fontWeight={"700"}>CHÀO MỪNG BẠN</Heading>
					<HStack spacing={4}>
						<Text fontSize={"16px"} fontWeight="600">
							{resultObj.name}
						</Text>
						<Menu>
							<MenuButton>
								<Box
									w={showSideBar ? "60px" : "50px"}
									h={showSideBar ? "60px" : "50px"}
									minWidth={showSideBar ? "40px" : "30px"}
								>
									{resultObj.avatar !== "" ? (
										<Img
											src={`${API_URL}${resultObj.avatar}`}
											w="100%"
											h="100%"
											objectFit="cover"
											borderRadius="50px"
										/>
									) : (
										<Avatar
											name={nameAvatar}
											size="10px"
											w="100%"
											h="100%"
											objectFit="cover"
											borderRadius="50px"
											bgColor={"#fefcf6"}
											color="#1a202c"
											border={"1px solid #d59457"}
										/>
									)}
								</Box>
							</MenuButton>
							<MenuList>
								<MenuItem color="#532d29" onClick={() => navigate("/update-info")}>
									Cập nhật thông tin
									<HStack marginLeft={"50px"}>
										<AiOutlineInfoCircle cursor="pointer" fontSize="20px" />
									</HStack>
								</MenuItem>
							</MenuList>
						</Menu>
					</HStack>
				</HStack>
				<Box
					w="100%"
					transition="all 0.2s ease"
					overflowY="auto"
					overflowX="hidden"
					h="100%"
					fontSize={{ sm: "16px", md: "16px" }}
					padding="0px"
					paddingBottom={"0px"}
					css={{
						"&::-webkit-scrollbar": {
							width: "4px"
						},
						"&::-webkit-scrollbar-track": {
							width: "6px"
						},
						"&::-webkit-scrollbar-thumb": {
							borderRadius: "24px"
						}
					}}
				>
					<VStack width={"100%"} height="100%">
						<OrderSummary />
						<LastTransaction />
					</VStack>
				</Box>
			</VStack>

			<RightSideBar width={"30%"} />
		</HStack>
	);
};
