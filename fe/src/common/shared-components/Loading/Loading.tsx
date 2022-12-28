import { Spinner, VStack } from "@chakra-ui/react";
import React from "react";

export const Loading = (): JSX.Element => {
	return (
		<VStack width={"100%"} height="100%" justifyContent={"center"} alignContent="center">
			<Spinner
				width={"60px"}
				height="60px"
				thickness="5px"
				speed="0.65s"
				emptyColor="gray.200"
				color="#d59457"
				size="500"
			/>
		</VStack>
	);
};
