import { Box, VStack, Text, Heading } from "@chakra-ui/react";
import React from "react";

import "../../../App.css";
import { ICardBack } from "./types";

export const CardBack: React.FC<ICardBack> = ({ title, description }) => {
	return (
		<Box width={"100%"} height="100%" backgroundColor={"#efeeec"} className="card-side side-back">
			<VStack width={"100%"} height="100%" alignContent={"center"} justifyContent="center">
				<Heading fontWeight={"400"} color="#f9d475" paddingBottom={"20px"}>
					{title}
				</Heading>
				<Text fontSize={"16px"} align="center" padding={"0px 10px"} color="#f9d475">
					{description}
				</Text>
			</VStack>
		</Box>
	);
};
