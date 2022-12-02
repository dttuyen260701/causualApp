
import { CSSProperties } from "react";
import {InputProps} from "@chakra-ui/input";

export interface IInput {
	label?: string;
	name: string;
	style?: CSSProperties;
	disabled?: boolean;
	inputProps?: InputProps;
	childrenIcon: JSX.Element;
}
