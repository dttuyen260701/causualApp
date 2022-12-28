import { Box, HStack, Text } from "@chakra-ui/react";
import React from "react";
import { NavLink } from "react-router-dom";

import { IItemSidebar } from "./sidebarTypes";

const ItemSidebar = ({ name, icon, navigate, showAll, handleClickItem }: IItemSidebar): JSX.Element => {
	return (
		<NavLink to={navigate}>
			{({ isActive }) => (
				<HStack
					padding="5px"
					cursor="pointer"
					spacing="10px"
					justifyContent={showAll === true ? "flex-start" : "center"}
					w={showAll === true ? "220px" : "55px"}
					borderWidth={isActive ? "0px" : "2px"}
					borderColor="transparent"
					borderRadius="3px"
					_hover={{
						bgColor: "rgba(255, 204, 51, 0.8)"
					}}
					onClick={handleClickItem}
				>
					<Box
						w="40px"
						h="40px"
						bgColor={"#sidebarBackground"}
						borderRadius="10px"
						display="flex"
						alignItems="center"
						justifyContent="center"
						fontSize="25px"
					>
						{icon}
					</Box>
					{showAll === true && (
						<Text fontWeight="700" fontSize="14px" textAlign={"left"}>
							{name}
						</Text>
					)}
				</HStack>
			)}
		</NavLink>
	);
};

export default ItemSidebar;
