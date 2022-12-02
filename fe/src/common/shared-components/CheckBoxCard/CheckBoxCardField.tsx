import { Box, useRadio } from "@chakra-ui/react";
import React from "react";

import { ICheckBoxCard } from "./CheckBoxCardTypes";

export const CheckBoxCardFied: React.FC<ICheckBoxCard> = props => {
	const { getInputProps, getCheckboxProps } = useRadio(props.radioProps);

	const input = getInputProps();
	const checkbox = getCheckboxProps();

	return (
		<Box as="label">
			<input {...input} />
			<Box
				{...checkbox}
				cursor="pointer"
				borderWidth="1px"
				borderRadius="md"
				boxShadow="md"
				height="100%"
				width="100%"
				_checked={{
					bg: "#E48D41",
					color: "white",
					borderColor: "#FDB493"
				}}
				_focus={{
					boxShadow: "#a35817"
				}}
				px={5}
				py={1}
			>
				{props.children}
			</Box>
		</Box>
	);
};
