import { HStack, useRadioGroup, Text, FormControl, FormErrorMessage } from "@chakra-ui/react";
import { Field, FieldProps, useFormikContext } from "formik";
import React from "react";

import { CheckBoxCardFied } from "../../common/shared-components/CheckBoxCard/CheckBoxCardField";

import { ICheckBoxGroup } from "./CheckBoxGroupType";

export const CheckBoxGroup: React.FC<ICheckBoxGroup> = props => {
	const options = ["Nam", "Nữ", "Khác"];

	const { setFieldValue } = useFormikContext();

	const handleChangeCheckBox = (value: string) => {
		setFieldValue(props.name, value);
	};

	const { getRootProps, getRadioProps } = useRadioGroup({
		name: props.name,
		defaultValue: props.valueProps,
		onChange: handleChangeCheckBox
	});

	const group = getRootProps();

	return (
		<Field name={props.name}>
			{({ field, form }: FieldProps) => {
				return (
					<FormControl isInvalid={!!form.errors[field.name] && !!form.touched[field.name]}>
						<Text paddingBottom={"5px"}>{props.label}</Text>
						<HStack {...group} justifyContent="center" width={"100%"}>
							{options.map(value => {
								const radio = getRadioProps({ value });
								return (
									<CheckBoxCardFied key={value} radioProps={radio}>
										{value}
									</CheckBoxCardFied>
								);
							})}
						</HStack>
						{field.value === "" && (
							<Text fontSize={"13px"} color="red" paddingTop={"5px"}>
								{form.errors[field.name] as string}
							</Text>
						)}
						{/*{field.value === "" && (
							<FormErrorMessage fontSize={"13px"}>{form.errors[field.name] as string}</FormErrorMessage>
						)}*/}
					</FormControl>
				);
			}}
		</Field>
	);
};
