import { Heading, HStack, Image, VStack, Box, Button } from "@chakra-ui/react";
import React from "react";
import { HiOutlineLogout } from "react-icons/hi";
import { IoIosArrowForward } from "react-icons/io";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";

import { getCurrentExpandSidebar, setExpandSidebar } from "../../../../app/reducer/expandSidebar/expandSidebarSlice";
import { logout } from "../../../../app/reducer/loginAuth/loginAuthSlice";
import { ItemSideBarList } from "../../../constants/ItemSideBarConst";

import ItemSidebar from "./ItemSidebar";

export const SideBar = (): JSX.Element => {
	const dispatch = useDispatch();
	const navigate = useNavigate();
	const isExpand: boolean = useSelector(getCurrentExpandSidebar);

	const handleLogout = (): void => {
		dispatch(logout());
		navigate("/login");
	};

	const handleClickSidebar = (): void => {
		dispatch(setExpandSidebar(!isExpand));
	};
	return (
		<VStack
			height={"100%"}
			w={isExpand ? "240px" : "90px"}
			transition="all 0.2s ease"
			spacing={"10px"}
			justify="center"
			bgColor="#f9d475"
			padding="5px 0px"
			boxShadow="8px 8px 10px rgb(249, 212, 117, 0.6)"
		>
			<HStack
				width={"100%"}
				spacing={isExpand ? 0 : 1}
				paddingLeft={isExpand ? "20px" : "15px"}
				alignContent="center"
				justify={"flex-end"}
				boxSizing="border-box"
			>
				<VStack width={"100%"}>
					<Box>
						<Image
							src="/images/logoWeb-removeBg.png"
							width={isExpand ? "100px" : "60px"}
							height={isExpand ? "100px" : "60px"}
						/>
					</Box>

					{isExpand && (
						<Heading color={"#000000"} fontSize="20px" fontWeight={"500"}>
							Quản lý thông tin
						</Heading>
					)}
				</VStack>
				<Box w="20%" position={"relative"} right={0}>
					<VStack
						w={isExpand ? "30px" : "10px"}
						h="40px"
						bgColor="#f9d475"
						align="center"
						justifyContent="center"
						marginLeft="auto"
						cursor={"pointer"}
						_hover={{
							transform: "translateY(-10%)",
							shadow: "xl"
						}}
						onClick={handleClickSidebar}
					>
						<IoIosArrowForward transform={isExpand ? "rotate(180)" : "rotate(0)"} />
					</VStack>
				</Box>
			</HStack>

			<Box width="100%" height={"100%"} overflow={"scroll"} paddingTop="20px">
				<VStack w="100%" cursor="pointer" justify="center" align="center">
					{ItemSideBarList.map((item, index) => (
						<ItemSidebar key={index} name={item.name} icon={item.icon} navigate={item.navigate} showAll={isExpand} />
					))}
				</VStack>
			</Box>

			<Box
				position={"relative"}
				bottom="0"
				width={"100%"}
				backgroundColor="#f9d475"
				padding={isExpand ? "0px 60px" : "0px 30px"}
			>
				<Button
					leftIcon={<HiOutlineLogout fontSize={"25px"} />}
					backgroundColor="#f9d475"
					padding="0px"
					onClick={handleLogout}
					_hover={{
						bgColor: "rgba(255, 204, 51, 0.8)"
					}}
				>
					{isExpand && "Log out"}
				</Button>
			</Box>
		</VStack>
	);
};
