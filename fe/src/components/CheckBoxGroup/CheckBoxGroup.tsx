import { HStack, useRadioGroup, Text, FormControl } from "@chakra-ui/react";
import { Field, FieldProps, useFormikContext } from "formik";
import React from "react";
import { useSelector } from "react-redux";

import { getCurrentWorker } from "../../app/reducer/loginAuth/loginAuthSlice";
import { GenderType } from "../../common/constants/constants";
import { CheckBoxCardFied } from "../../common/shared-components/CheckBoxCard/CheckBoxCardField";

import { ICheckBoxGroup } from "./CheckBoxGroupType";

export const CheckBoxGroup: React.FC<ICheckBoxGroup> = props => {
	const options = GenderType.map(item => item.type);
	const resultObj = useSelector(getCurrentWorker);

	const { setFieldValue } = useFormikContext();

	const handleChangeCheckBox = (value: string) => {
		setFieldValue(props.name, value);
	};

	const { getRootProps, getRadioProps } = useRadioGroup({
		name: props.name,
		defaultValue: GenderType.find(item => item.id === resultObj.gender)?.type,
		onChange: handleChangeCheckBox
	});

	const group = getRootProps();

	return (
		<Field name={props.name}>
			{({ field, form }: FieldProps) => {
				return (
					<FormControl isInvalid={!!form.errors[field.name] && !!form.touched[field.name]}>
						<Text paddingBottom={"5px"}>{props.label}</Text>
						<HStack {...group} justifyContent="center" width={"100%"} spacing={10}>
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
					</FormControl>
				);
			}}
		</Field>
	);
};
