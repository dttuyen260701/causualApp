import { InputProps } from "@chakra-ui/input/dist/declarations/src/input";
import { CSSProperties } from "react";

export interface IInput {
	label?: string;
	name: string;
	style?: CSSProperties;
	disabled?: boolean;
	inputProps?: InputProps;
	childrenIcon: JSX.Element;
}
