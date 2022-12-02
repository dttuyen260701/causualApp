import { Box, Img, VStack, Text, HStack, Heading } from "@chakra-ui/react";
import React from "react";

import "../../../App.css";
import { ICardBack } from "./types";

export const CardBack: React.FC<ICardBack> = ({ title, description }) => {
	return (
		<Box width={"100%"} height="100%" backgroundColor={"#efeeec"} className="card-side side-back">
			<VStack width={"100%"} height="100%" alignContent={"center"} justifyContent="center">
				{/* Picture */}
				<Heading fontWeight={"400"} color="#e48d41" paddingBottom={"20px"}>
					{title}
				</Heading>
				<Text fontSize={"16px"} align="center" padding={"0px 10px"} color="#e48d41">
					{description}
				</Text>
			</VStack>
		</Box>
	);
};
