import { Box, VStack, Text } from "@chakra-ui/react";
import React from "react";
import { MdSystemSecurityUpdateGood } from "react-icons/md";

import { IFeatureCard } from "./types";

export const FeatureCard: React.FC<IFeatureCard> = ({ content, icon }) => {
	return (
		<VStack
			width={"25%"}
			backgroundColor={"#faf2e5"}
			padding="10px"
			borderRadius={"10px"}
			boxShadow="0px 0px 10px #f6d79f"
		>
			<Box>
				{/*<MdSystemSecurityUpdateGood color="#d49457" size={"30"} />*/}
				{icon}
			</Box>
			<Text align={"center"} color="#9c7d6e">
				{content}
			</Text>
		</VStack>
	);
};
