import { Box, HStack, useMediaQuery, VStack } from "@chakra-ui/react";
import React from "react";
import { Outlet } from "react-router-dom";

import { useGetRightWidth } from "../../hooks/useGetRightWidth";

import Navbar from "./navbar/Navbar";
import { SideBar } from "./sidebar/Sidebar";

export const MainLayout = (): JSX.Element => {
	const [showSideBar] = useMediaQuery("(min-width: 1100px)");
	const rightWidth = useGetRightWidth();

	return (
		<HStack minH={"100vh"} alignItems="flex-start" spacing={0} h="100vh" bgColor={"#f9faf5"}>
			{showSideBar && <SideBar />}
			<VStack width="100%" alignItems="flex-start" height="100%" spacing="0" transition="all 0.2s ease">
				{!showSideBar && <Navbar />}
				<Box
					w="100%"
					transition="all 0.2s ease"
					width={rightWidth}
					overflowY="auto"
					overflowX="hidden"
					//  bgColor="#f9f4f0"
					h="100%"
					fontSize={{ sm: "16px", md: "16px" }}
					padding="20px"
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
		</HStack>
	);
};
