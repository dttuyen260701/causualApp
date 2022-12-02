import { Box, Img, VStack, Text, HStack, Heading } from "@chakra-ui/react";
import React from "react";

import { API_URL } from "../../utils/config";

import { ICardFront } from "./types";

export const CardFront: React.FC<ICardFront> = ({ title, image }) => {
	console.log("image: ", `${API_URL}${image}`);

	return (
		<Box width={"100%"} height="100%" backgroundColor={"#e48d41"} className="card-side side-front">
			<VStack width={"100%"} height="100%">
				{/* Picture */}
				<Img
					//w={{ md: "230px", lg: "300px" }} h={{ md: "230px", lg: "300px" }}
					width={"100%"}
					height="100%"
					//src="/images/bgUs.jpg"
					src={`${API_URL}${image}`}
				/>
				<VStack width={"100%"} height="100%" padding={"30px 10px"}>
					<Heading fontSize={"30px"} fontWeight="400">
						{title}
					</Heading>
					{/*<Text fontSize={"16px"} align="center" padding={"0px 10px"}>
						Helping Those Who Need Us Most Helping
					</Text>*/}
				</VStack>
			</VStack>
		</Box>
	);
};
