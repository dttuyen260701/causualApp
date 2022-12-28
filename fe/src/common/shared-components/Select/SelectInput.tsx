import React, { useState } from "react";
import Select from "react-select";
import { Box } from "@chakra-ui/react";

import useInputSelectStyle from "../../hooks/useInputSelectStyle";

import { IOption, ISelect } from "./SelectTypes";

const SelectInput: React.FC<ISelect> = ({ width, options, placeholder, onChange, onInputChange, value }) => {
	const placeholderOption: IOption = { label: placeholder ?? "", value: value ?? "" };
	const [valueSelect, setValueSelect] = useState<IOption>(placeholderOption);

	const { selectStyle } = useInputSelectStyle({ style: { height: "56px" } });

	const onChangeSelect = (options: IOption | null) => {
		if (onChange) {
			onChange(options ?? { value: "", label: "" });
		}
		const value: IOption = {
			label: options?.label ?? (placeholder || ""),
			value: options?.value ?? ""
		};
		setValueSelect(value);
	};

	const customOptions: IOption[] = [{ label: placeholder ?? "", value: "" }, ...options];

	return (
		<Box
			width={{ md: width ?? "100%", base: "100%" }}
			position={"relative"}
			borderColor={"#6a5af9"}
			borderWidth={"1px"}
			height={"56px"}
			borderRadius={"5px"}
			_hover={{
				shadow: "md",
				borderColor: "#6a5af9",
				borderWidth: "2px",
				boxShadow: "0px 0px 13px -3px #6a5af9"
			}}
		>
			<Select
				options={customOptions}
				placeholder={placeholder}
				theme={theme => ({
					...theme,
					height: "56px",
					borderWidth: 0,
					boxShadow: "none"
				})}
				styles={selectStyle}
				onChange={onChangeSelect}
				onInputChange={e => {
					if (onInputChange) {
						onInputChange(e);
					}
				}}
				value={value === "" ? placeholderOption : valueSelect}
			/>
		</Box>
	);
};

export default SelectInput;
