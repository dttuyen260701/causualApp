import { Box, Heading, VStack, Text, HStack } from "@chakra-ui/react";
import React from "react";
import { AiFillStar } from "react-icons/ai";

import { IQualityCard } from "./types";

export const QualityCard: React.FC<IQualityCard> = ({ title, content }: IQualityCard) => {
	return (
		<Box width={"100%"} height="100%" backgroundColor={"#fdf8ea"} borderRadius={"10px"} boxShadow="0px 7px 7px #f6d79f">
			<VStack
				width={"100%"}
				height="100%"
				justifyContent={"space-between"}
				alignContent="flex-start"
				paddingBottom={"20px"}
			>
				<Box
					bgColor={"#f9d475"}
					width="40px"
					height={"40px"}
					position="relative"
					top="-10%"
					left={"-30%"}
					borderRadius="15px"
				>
					<AiFillStar size={"40"} color="#faf2e5" />
				</Box>
				<HStack width="100%" height={"100%"} alignContent="flex-start" justifyContent={"flex-start"} paddingLeft="10px">
					<Heading fontSize={"30px"} fontWeight="400" color="#000000">
						{title}
					</Heading>
				</HStack>
				<HStack height={"100%"} alignContent="center" justifyContent={"center"}>
					<Text fontSize={"16px"} align="left" padding={"0px 10px"} color="#9c7d6e">
						{content}
					</Text>
				</HStack>
			</VStack>
		</Box>
	);
};
