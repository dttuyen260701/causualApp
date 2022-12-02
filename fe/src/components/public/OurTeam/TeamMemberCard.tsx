import { Box, Heading, Img, VStack, Text } from "@chakra-ui/react";
import React from "react";

import { IMemberCard } from "./types";

export const TeamMemberCard: React.FC<IMemberCard> = ({ image }) => {
	return (
		<Box width={"100%"} height="100%" backgroundColor={"#fffcf8"}>
			<VStack width={"100%"} height="100%">
				{/* Picture */}
				<Img
					//w={{ md: "230px", lg: "300px" }} h={{ md: "230px", lg: "300px" }}
					width={"100%"}
					height="100%"
					//src="/images/bgUs.jpg"
					src={image}
				/>
			</VStack>
		</Box>
	);
};
