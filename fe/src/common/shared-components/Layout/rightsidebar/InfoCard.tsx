import { Box, HStack, VStack, Text } from "@chakra-ui/react";
import React from "react";

import { IInfoCard } from "./type";

export const InfoCard: React.FC<IInfoCard> = props => {
	return (
		<HStack
			padding={"10px"}
			width="100%"
			height={"100%"}
			justifyContent={"flex-start"}
			spacing={5}
			borderBottom="1px solid #d0c5ba"
		>
			<Box bgColor={"#d0c5ba"} padding="10px" borderRadius={"30px"}>
				{props.icon}
			</Box>
			<VStack spacing={0}>
				<Text width={"100%"} textAlign="left" color={"#a6a09a"} fontSize="14px">
					{props.title}
				</Text>
				<Text width={"100%"} textAlign="left" fontSize={"15px"}>
					{props.detail}
				</Text>
			</VStack>
		</HStack>
	);
};
