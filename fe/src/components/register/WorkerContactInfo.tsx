import { Button, Flex, HStack, VStack, Text } from "@chakra-ui/react";
import React, { useState } from "react";
import { Form, Formik, FormikErrors, FormikHelpers } from "formik";
import { GrCaretPrevious } from "react-icons/gr";
import { MdOutlineContactPhone } from "react-icons/md";
import { AiOutlinePhone } from "react-icons/ai";
//import { FaCity } from "react-icons/fa";
import { TbAddressBook } from "react-icons/tb";

import { IUserContactRegister } from "../../app/api/register/registerType";
import InputField from "../../common/shared-components/Input/InputField";
import SelectInputField from "../../common/shared-components/Select/SelectInputField";
import { IOption } from "../../common/shared-components/Select/SelectTypes";

interface IWorkerContactInfo {
	handleBackPage: (page: number) => void;
}

const WorkerContactInfo: React.FC<IWorkerContactInfo> = props => {
	const handleBackPage = () => {
		props.handleBackPage(2);
	};
	const [selectedLocationId, setSelectedLocationId] = useState<string>("");
	const [isCheckType, setIsCheckType] = useState<boolean>(false);
	const locationOptions: IOption[] = [
		{ value: "1", label: "DN" },
		{ value: "2", label: "SG" },
		{ value: "3", label: "HN" }
	];
	const handleInputChangeLocation = (currentId: string) => {
		if (currentId) {
			setIsCheckType(true);
		} else {
			setIsCheckType(false);
		}
	};

	const handleSubmit = async (values: IUserContactRegister, actions: FormikHelpers<IUserContactRegister>) => {
		alert(values);
	};
	const initialValues: IUserContactRegister = {
		PhoneNumber: "",
		Address: "",
		AddressPoint: "",
		Province: "",
		District: "",
		Commune: ""
	};
	return (
		<Formik
			initialValues={initialValues}
			validate={values => {
				const error: FormikErrors<IUserContactRegister> = {};
				if (!values.PhoneNumber) {
					error.PhoneNumber = "Phone number is required";
				}
				if (values.PhoneNumber && !/^\d{10}$/.test(values.PhoneNumber.trim())) {
					error.PhoneNumber = "Phone Number must be number";
				}
				if (!values.Address) {
					error.Address = "Address is required";
				}
				if (!values.Province) {
					error.Province = "Province is required";
				}
				if (!values.District) {
					error.District = "District is required";
				}
				if (!values.Commune) {
					error.Commune = "Commune is required";
				}
				return error;
			}}
			onSubmit={handleSubmit}
		>
			{() => (
				<Form>
					<Flex width={"100%"} height={"100%"} bgColor={"white"} justifyContent={"center"}>
						<VStack
							width={"100%"}
							height={"100%"}
							justifyContent={"center"}
							boxSizing="border-box"
							//spacing={2}
							position={"absolute"}
							padding="0 50px"
						>
							{/* Heading */}
							<HStack padding={"0px 0 20px"} justifyContent={"flex-start"} width="100%" spacing={4}>
								<MdOutlineContactPhone color="#E48D41" size={"30px"} />
								<Text
									fontFamily={"'Bungee', monospace"}
									fontWeight="700"
									fontSize={{ sm: "4.4vw", md: "30px", lg: "45px" }}
									bgClip="text"
									color={"#E48D41"}
								>
									Thông tin liên lạc
								</Text>
							</HStack>
							<InputField
								name="PhoneNumber"
								label="Số điện thoại"
								inputProps={{
									fontSize: "18px",
									height: "45px",
									fontWeight: 400,
									width: "100%",
									color: "rgba(0, 0, 0)",
									maxLength: 11
								}}
								childrenIcon={<AiOutlinePhone />}
							/>
							<HStack spacing={6} width={"100%"}>
								<SelectInputField
									width="100%"
									label="Thành phố"
									placeholder="Chọn thành phố"
									name="Province"
									options={locationOptions}
									onChange={currentId => {
										setSelectedLocationId(currentId);
										// setCurrentPage(1);
									}}
									onInputChange={e => {
										handleInputChangeLocation(e);
									}}
									style={{ height: "45px" }}
								/>
								<SelectInputField
									width="100%"
									label="Quận"
									placeholder="Chọn quận"
									name="District"
									options={locationOptions}
									onChange={currentId => {
										setSelectedLocationId(currentId);
										// setCurrentPage(1);
									}}
									onInputChange={e => {
										handleInputChangeLocation(e);
									}}
									style={{ height: "45px" }}
								/>
							</HStack>
							<HStack spacing={6} width={"100%"}>
								<SelectInputField
									width="100%"
									label="Huyện"
									placeholder="Chọn huyện"
									name="Commune"
									options={locationOptions}
									onChange={currentId => {
										setSelectedLocationId(currentId);
										// setCurrentPage(1);
									}}
									onInputChange={e => {
										handleInputChangeLocation(e);
									}}
									style={{ height: "45px" }}
								/>
								<InputField
									name="Address"
									label="Địa chỉ"
									inputProps={{
										fontSize: "18px",
										height: "45px",
										fontWeight: 400,
										width: "100%",
										color: "rgba(0, 0, 0)"
									}}
									childrenIcon={<TbAddressBook />}
								/>
							</HStack>
							<HStack justifyContent={"center"} width="100%" paddingTop={"20px"} paddingBottom={"50px"}>
								<Button
									//marginTop={"10px"}
									color={"#fff"}
									borderRadius="20px"
									//bgGradient={"linear(to-tr, #d05d9c, #f8bb1c)"}
									bgColor="#E48D41"
									_hover={{
										bgColor: "rgba(222, 116, 24)"
									}}
									_active={{
										bgColor: "rgba(222, 116, 24)"
									}}
									width={{ base: "100%", md: "48%" }}
									height={"50px"}
									//leftIcon={<BiSave />}
									type="submit"
									//isLoading={isLoading}
									//isDisabled={!isValueChanged || isDisabled}
								>
									ĐĂNG KÝ
								</Button>
							</HStack>
							<HStack justifyContent={"flex-start"} width="100%">
								<Flex
									bgColor={"rgba(222, 116, 24)"}
									width="50px"
									height="50px"
									borderRadius={"30px"}
									alignItems="center"
									justifyContent={"center"}
									cursor="pointer"
									boxShadow="4px 4px 6px #FDB493"
									_hover={{
										bgColor: "#E48D41"
									}}
									onClick={handleBackPage}
								>
									<GrCaretPrevious color="white" width="50px" height="50px" />
								</Flex>
							</HStack>
						</VStack>
					</Flex>
				</Form>
			)}
		</Formik>
	);
};

export default WorkerContactInfo;
