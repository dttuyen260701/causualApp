import { Box, HStack, Menu, MenuButton, MenuDivider, MenuItem, MenuList, useMediaQuery } from "@chakra-ui/react";
import React from "react";
import { FiLogOut } from "react-icons/fi";
import { AiOutlineMenu } from "react-icons/ai";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";

import { ItemSideBarList } from "../../../constants/ItemSideBarConst";
import { setNameItemSideBar } from "../../../../app/reducer/nameItemSideBar/nameItemSideBarSlice";
import { logout } from "../../../../app/reducer/loginAuth/loginAuthSlice";

import { IProfileNavbar } from "./type";

const ProfileNavbar: React.FC<IProfileNavbar> = () => {
	const [showSideBar] = useMediaQuery("(min-width: 1100px)");
	const navigate = useNavigate();
	const dispatch = useDispatch();
	const handleLogout = (): void => {
		dispatch(logout());
		navigate("/login");
	};
	return (
		<HStack
			textAlign="center"
			spacing="5px"
			w={{ sm: "30%", md: "50%" }}
			justify="flex-end"
			fontSize="16px"
			fontWeight="500"
		>
			<HStack width="550px" justify="flex-end" spacing="15px">
				{!showSideBar && (
					<HStack paddingLeft={{ sm: "5px", md: "20px" }} spacing="15px">
						<Menu>
							<MenuButton>
								<Box>
									<AiOutlineMenu />
								</Box>
							</MenuButton>
							<MenuList>
								<Box
									css={{
										"&::-webkit-scrollbar": {
											width: "4px"
										},
										"&::-webkit-scrollbar-track": {
											width: "6px"
										},
										"&::-webkit-scrollbar-thumb": {
											background: "#8ccef0",
											borderRadius: "24px"
										}
									}}
									overflowX="auto"
									maxHeight="200px"
								>
									{ItemSideBarList.map((item, index) => (
										<MenuItem
											key={index}
											pt={1}
											pb={1}
											onClick={() => {
												dispatch(setNameItemSideBar(item.name));
												navigate(item.navigate);
											}}
										>
											<HStack marginRight={"20px"}>{item.icon}</HStack>
											{item.name}
										</MenuItem>
									))}
									<MenuDivider />
									<MenuItem color="blueColor" onClick={handleLogout}>
										Log out
										<HStack marginLeft={"150px"}>
											<FiLogOut cursor="pointer" fontSize="16px" />
										</HStack>
									</MenuItem>
								</Box>
							</MenuList>
						</Menu>
					</HStack>
				)}
			</HStack>
		</HStack>
	);
};

export default ProfileNavbar;
