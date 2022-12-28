import { Box, Img, VStack } from "@chakra-ui/react";
import React from "react";

import { IMemberCard } from "./types";

export const TeamMemberCard: React.FC<IMemberCard> = ({ image }) => {
	return (
		<Box width={"100%"} height="100%" backgroundColor={"#fffcf8"}>
			<VStack width={"100%"} height="100%">
				{/* Picture */}
				<Img width={"100%"} height="100%" src={image} />
			</VStack>
		</Box>
	);
};
