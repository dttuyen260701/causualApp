import { Box, Img, VStack, Heading } from "@chakra-ui/react";
import React from "react";

import { API_URL } from "../../utils/config";

import { ICardFront } from "./types";

export const CardFront: React.FC<ICardFront> = ({ title, image }) => {
	return (
		<Box width={"100%"} height="100%" backgroundColor={"#f9d475"} className="card-side side-front">
			<VStack width={"100%"} height="100%">
				<Img width={"100%"} height="100%" src={`${API_URL}${image}`} />
				<VStack width={"100%"} height="100%" padding={"30px 10px"}>
					<Heading fontSize={"30px"} fontWeight="400">
						{title}
					</Heading>
				</VStack>
			</VStack>
		</Box>
	);
};
