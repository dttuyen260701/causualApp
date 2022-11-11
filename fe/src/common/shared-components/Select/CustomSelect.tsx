import {
	Box,
	Button,
	Menu,
	MenuButton,
	MenuItemOption,
	MenuList,
	MenuOptionGroup,
	Text,
	VStack
} from "@chakra-ui/react";
import React, { useState } from "react";
import { FiChevronDown } from "react-icons/fi";

import { IOption, ISelect } from "./SelectTypes";

const CustomSelect: React.FC<ISelect> = ({ options, onChange, placeholder, value, label, style }) => {
	const currentOption: IOption | undefined = options.find(o => o.value === value);
	const [isShowOptions, setIsShowOptions] = useState<boolean>(false);

	return (
		<VStack>
			{label && (
				<Text
					lineHeight={"20px"}
					marginBottom={"8px"}
					marginTop={{ base: "16px", md: 0 }}
					color={"secondary"}
					fontSize={"14px"}
					fontWeight={500}
				>
					{label}
				</Text>
			)}
			<Box w="100%">
				<Menu autoSelect={false} preventOverflow={false}>
					<MenuButton
						as={Button}
						height={"56px"}
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
							borderColor: "#6a5af9",
							borderWidth: "2px",
							boxShadow: "0px 0px 13px -3px #6a5af9"
						}}
						_active={{ borderWidth: 2 }}
						style={style}
					>
						{currentOption ? currentOption?.label : placeholder}
					</MenuButton>
					{isShowOptions && (
						<MenuList>
							<MenuOptionGroup value={value ? currentOption?.value : ""} overflow={{ base: "visible", lg: "auto" }}>
								{placeholder && (
									<MenuItemOption
										key={`${placeholder}`}
										value={""}
										onClick={() => {
											if (onChange) {
												onChange("");
											}
											setIsShowOptions(false);
										}}
									>
										{placeholder}
									</MenuItemOption>
								)}
								{options.map((option, index) => (
									<MenuItemOption
										key={`${option.value}-${index}`}
										value={option.value}
										onClick={() => {
											if (onChange) {
												onChange(option.value);
											}
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
		</VStack>
	);
};

export default CustomSelect;
