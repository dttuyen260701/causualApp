import { CSSProperties } from "react";

export interface IOption {
	value: string;
	label: string;
}

export interface ISelect {
	options: IOption[];
	label?: string;
	placeholder?: string;
	name?: string;
	width?: string;
	onChange?: (value: string) => void;
	value?: string;
	onInputChange?: (value: string) => void;
	style?: CSSProperties;
}
