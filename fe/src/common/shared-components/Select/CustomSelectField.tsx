import React, { useState } from "react";
import { Field, FieldProps } from "formik";
import {
	Box,
	Button,
	FormControl,
	FormErrorMessage,
	FormLabel,
	HStack,
	Menu,
	MenuButton,
	MenuItemOption,
	MenuList,
	MenuOptionGroup
} from "@chakra-ui/react";
import { FiChevronDown } from "react-icons/fi";

import { IOption, ISelect } from "./SelectTypes";

const CustomSelectField: React.FC<ISelect> = props => {
	const name = props.name ?? "";
	const id = `id-${props.name}`;

	const [isShowOptions, setIsShowOptions] = useState<boolean>(false);

	return (
		<Field name={name}>
			{({ field, form }: FieldProps) => {
				const currentOption: IOption | undefined = props.options.find(o => o.value === field.value);

				return (
					<FormControl isInvalid={!!form.errors[name] && !!form.touched[name]} width={props.width}>
						<HStack
							width={"100%"}
							flexDir={{ base: "column", md: "row" }}
							alignItems={{ base: "flex-start", md: "center" }}
							spacing={0}
							marginBottom={"8px"}
							py={"1px"}
						>
							{props.label && (
								<FormLabel htmlFor={id} lineHeight={"20px"} color={"secondary"} fontSize={"14px"} width={"90px"}>
									{props.label}
								</FormLabel>
							)}
							<Box w="100%">
								<Menu autoSelect={false} preventOverflow={false}>
									<MenuButton
										as={Button}
										height={"40px"}
										variant="outline"
										w="100%"
										onClick={() => setIsShowOptions(true)}
										textAlign={"left"}
										rightIcon={<FiChevronDown />}
										fontSize={"16px"}
										fontWeight={"500"}
										borderColor={"primary"}
										_hover={{
											shadow: "md",
											borderColor: "primary",
											borderWidth: "2px",
											boxShadow: "0px 0px 13px -3px #6a5af9"
										}}
										_focus={{
											borderColor: "primary",
											borderWidth: "2px"
										}}
										_active={{ borderWidth: 2 }}
										style={props.style}
									>
										{currentOption?.label}
									</MenuButton>
									{isShowOptions && (
										<MenuList>
											<MenuOptionGroup defaultValue={currentOption?.value} overflow={{ base: "visible", lg: "auto" }}>
												{props.options.map((option, index) => (
													<MenuItemOption
														key={`${option.value}-${index}`}
														value={option.value}
														onClick={() => {
															form.setFieldValue(name, option.value);
															setIsShowOptions(false);
														}}
													>
														{option.label}
													</MenuItemOption>
												))}
											</MenuOptionGroup>
										</MenuList>
									)}
								</Menu>
							</Box>
						</HStack>
						<FormErrorMessage fontSize="16px">{form.errors[name] as string}</FormErrorMessage>
					</FormControl>
				);
			}}
		</Field>
	);
};

export default CustomSelectField;
