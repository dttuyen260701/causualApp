import { Box, VStack, Text } from "@chakra-ui/react";
import React from "react";

import { IFeatureCard } from "./types";

export const FeatureCard: React.FC<IFeatureCard> = ({ content, icon }) => {
	return (
		<VStack
			width={"25%"}
			backgroundColor={"#faf2e5"}
			padding="10px"
			borderRadius={"10px"}
			boxShadow="0px 0px 10px #f9d475"
		>
			<Box>{icon}</Box>
			<Text align={"center"} color="#000000">
				{content}
			</Text>
		</VStack>
	);
};
