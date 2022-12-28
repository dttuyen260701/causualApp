import { Box, HStack, Img, useMediaQuery, VStack, Text, Button, Avatar } from "@chakra-ui/react";
import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { Outlet, useNavigate } from "react-router-dom";

import { getCurrentWorker } from "../app/reducer/loginAuth/loginAuthSlice";
import { API_URL } from "../common/utils/config";

export const UpdateInfo: React.FC = () => {
	const [showSideBar] = useMediaQuery("(min-width: 1100px)");
	const navigate = useNavigate();
	const resultObj = useSelector(getCurrentWorker);
	const [nameAvatar, setNameAvatar] = useState("");

	useEffect(() => {
		const data = resultObj.name.split(" ");
		data.length > 1 ? setNameAvatar(data[0] + " " + data[data.length - 1]) : setNameAvatar(data[0]);
	}, [resultObj.name]);

	return (
		<VStack
			width={"100%"}
			height="100%"
			padding={"5px"}
			paddingBottom="10px"
			justifyContent="space-evenly"
			alignItems={"center"}
		>
			<HStack width={"100%"} height="20%" spacing={10}>
				<Box
					w={showSideBar ? "120px" : "100px"}
					h={showSideBar ? "120px" : "100px"}
					minWidth={showSideBar ? "40px" : "30px"}
				>
					{resultObj.avatar !== "" ? (
						<Img src={`${API_URL}${resultObj.avatar}`} w="100%" h="100%" objectFit="cover" borderRadius="100px" />
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
				<VStack width={"70%"} alignItems="flex-start">
					<Text fontWeight={"500"} color={"#000000"} fontSize={"30px"}>
						{resultObj.name}
					</Text>
					<HStack>
						<Button
							width={"50%"}
							backgroundColor="#f9faf5"
							border={"1px solid #000000"}
							borderRadius="16px"
							onClick={() => navigate("/update-info/account")}
						>
							<Text color="#000000">Thông tin tài khoản</Text>
						</Button>
						<Button
							width={"50%"}
							backgroundColor="#f9faf5"
							border={"1px solid #f9d475"}
							borderRadius="16px"
							onClick={() => navigate("/update-info/personal")}
						>
							<Text color="#f9d475">Thông tin cá nhân</Text>
						</Button>
					</HStack>
				</VStack>
			</HStack>
			<Box
				w="100%"
				transition="all 0.2s ease"
				overflowY="auto"
				overflowX="hidden"
				h="100%"
				fontSize={{ sm: "16px", md: "16px" }}
				padding="0px"
				paddingTop={"10px"}
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
				<Outlet />
			</Box>
		</VStack>
	);
};
