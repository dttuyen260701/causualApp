import { Box, VStack } from "@chakra-ui/react";
import React from "react";
import { Outlet } from "react-router-dom";

import { Navbar } from "../components/public/Navbar/Navbar";

const PublicPage = () => {
	return (
		<VStack
			width="100vw"
			alignItems="flex-start"
			height="100%"
			spacing="0"
			transition="all 0.2s ease"
			bgColor="background"
			minH="100vh"
			h="100vh"
		>
			<Navbar />
			<Box
				w="100%"
				transition="all 0.2s ease"
				width={"100%"}
				overflowY="auto"
				overflowX="hidden"
				h="100%"
				fontSize={{ sm: "16px", md: "16px" }}
				//padding="20px"
				css={{
					"&::-webkit-scrollbar": {
						width: "4px"
					},
					"&::-webkit-scrollbar-track": {
						width: "6px"
					},
					"&::-webkit-scrollbar-thumb": {
						background: "#6a5af9",
						borderRadius: "24px"
					}
				}}
			>
				<Outlet />
			</Box>
		</VStack>
	);
};

export default PublicPage;
