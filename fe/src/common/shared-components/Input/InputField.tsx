import React from "react";
import { FormControl, FormErrorMessage, Input, Text, InputGroup, InputLeftElement } from "@chakra-ui/react";
import { Field, FieldProps } from "formik";

import { IInput } from "./InputFieldTypes";

const InputField: React.FC<IInput> = props => {
	return (
		<Field name={props.name}>
			{({ field, form }: FieldProps) => {
				return (
					<FormControl isInvalid={!!form.errors[field.name] && !!form.touched[field.name]}>
						<Text paddingBottom={"5px"} lineHeight={"20px"}>
							{props.label}
						</Text>
						<InputGroup width={props.inputProps?.width}>
							<InputLeftElement pointerEvents={"none"}>{props.childrenIcon}</InputLeftElement>
							<Input
								{...field}
								focusBorderColor={"none"}
								borderRadius={"15px"}
								maxLength={50}
								borderColor={"primary"}
								_focus={{
									borderColor: "#f9d475",
									borderWidth: "2px"
								}}
								_hover={{
									shadow: "md",
									borderColor: "#f9d475",
									boxShadow: "0px 0px 13px -3px #FDB493"
								}}
								style={props.style}
								{...props.inputProps}
							/>
						</InputGroup>
						<FormErrorMessage fontSize="13px">{form.errors[field.name] as string}</FormErrorMessage>
					</FormControl>
				);
			}}
		</Field>
	);
};

export default InputField;
