import { Field, FieldProps } from "formik";
import React from "react";
import { Box, FormControl, FormErrorMessage, FormLabel, HStack, VStack } from "@chakra-ui/react";
import Select from "react-select";

import useInputSelectStyle from "../../utils/useInputSelectStyle";

import { ISelect } from "./SelectTypes";

const SelectInputField: React.FC<ISelect> = props => {
	const name = props.name ?? "";
	const id = `id-${props.name}`;
	const { selectStyle } = useInputSelectStyle({
		style: { height: props.style?.height, borderColor: "transparent" }
	});

	return (
		<Field name={name}>
			{({ field, form }: FieldProps) => {
				const currentOption =
					field.value && field.value.value ? field.value : props.options.find(option => option.value === field.value);

				return (
					<FormControl
						position={"relative"}
						isInvalid={!!form.errors[name] && !!form.touched[name]}
						width={props.width}
						onClick={() => {
							if (!form.touched[name] && currentOption == null) {
								form.setFieldTouched(name, false, true);
							}
						}}
					>
						<VStack
							width={props.width ?? "100%"}
							flexDir={{ base: "column", md: "column" }}
							alignItems={{ base: "center", md: "flex-start" }}
							spacing={0}
							//marginBottom={"8px"}
						>
							{props.label && (
								<FormLabel
									htmlFor={id}
									lineHeight={"20px"}
									color={"rgba(0, 0, 0)"}
									fontWeight={"400"}
									fontSize={"16px"}
									width={"90px"}
								>
									{props.label}
								</FormLabel>
							)}
							<Box
								width={"100%"}
								position={"relative"}
								borderColor={!!form.errors[name] && !!form.touched[name] ? "errorColor" : "primary"}
								borderWidth={!!form.errors[name] && !!form.touched[name] ? "2px" : "1px"}
								border={"1px solid"}
								borderRadius="15px"
								_hover={{
									shadow: "md",
									borderWidth: "2px",
									borderColor: "#E48D41",
									boxShadow: "0px 0px 13px -3px #FDB493"
								}}
								p={0}
								style={props.style}
							>
								<Select
									options={props.options}
									placeholder={props.placeholder}
									theme={theme => ({
										...theme,
										borderRadius: 8,
										colors: {
											...theme.colors,
											primary25: "#D8E3FF",
											primary: "#3B5998"
										}
									})}
									styles={selectStyle}
									{...field}
									onBlur={() => {
										if (!form.touched[name] && currentOption == null) {
											form.setFieldTouched(name, true, true);
										}
									}}
									onChange={e => {
										if (props.onChange) {
											props.onChange(e.value);
										}
										if (name != null) {
											form.setFieldValue(name, e.value);
										}
									}}
									onInputChange={e => {
										if (props.onInputChange) {
											props.onInputChange(e);
										}
									}}
									value={currentOption}
								/>
							</Box>
						</VStack>

						<FormErrorMessage fontSize="16px">{form.errors[name] as string}</FormErrorMessage>
					</FormControl>
				);
			}}
		</Field>
	);
};

export default SelectInputField;
