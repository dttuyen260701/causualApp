import { Button, Flex, HStack, VStack, Text } from "@chakra-ui/react";
import React, { useState } from "react";
import { Form, Formik, FormikErrors, FormikHelpers } from "formik";
import { GrCaretPrevious } from "react-icons/gr";
import { MdOutlineContactPhone } from "react-icons/md";
import { AiOutlinePhone } from "react-icons/ai";
import { TbAddressBook } from "react-icons/tb";
import { useNavigate } from "react-router-dom";
import { format } from "date-fns";

import { IUserContactRegister, IUserLoginRegister, IUserPersonalRegister } from "../../app/api/register/registerType";
import InputField from "../../common/shared-components/Input/InputField";
import SelectInputField from "../../common/shared-components/Select/SelectInputField";
import { IOption } from "../../common/shared-components/Select/SelectTypes";
import { useRegisterMutation } from "../../app/api/register/registerApi";
import { userContactInfoEmpty } from "../../common/constants/constants";
import { useGetAllProvincesQuery } from "../../app/api/provinces/provinceApi";
import { useGetDistrictByIdProvinceQuery } from "../../app/api/districts/districtApi";
import { useGetWardByIdDistrictQuery } from "../../app/api/wards/wardApi";
import { IProvince } from "../../app/api/provinces/provinceType";
import { IDistrict } from "../../app/api/districts/districtTypes";
import { IWard } from "../../app/api/wards/wardTypes";

interface IWorkerContactInfo {
	handleBackPage: (page: number) => void;
	userLoginInfo: IUserLoginRegister;
	userPersonalInfo: IUserPersonalRegister;
	handleGetContactInfo: (values: IUserContactRegister) => void;
	userContactInfo: IUserContactRegister;
}

const WorkerContactInfo: React.FC<IWorkerContactInfo> = props => {
	const [registerWorker] = useRegisterMutation();
	const navigate = useNavigate();
	const [selectedProvince, setSelectedProvince] = useState<IOption>({ value: "", label: "" });
	const [selectedDistrict, setSelectedDistrict] = useState<IOption>({ value: "", label: "" });
	const [selectedWard, setSelectedWard] = useState<IOption>({ value: "", label: "" });
	const [isCheckType, setIsCheckType] = useState<boolean>(false);
	const { data: provinceData } = useGetAllProvincesQuery();
	const { data: districtData } = useGetDistrictByIdProvinceQuery(selectedProvince.value);
	const { data: wardData } = useGetWardByIdDistrictQuery(selectedDistrict.value);
	const provinceOptions: IOption[] = [];
	const districtOptions: IOption[] = [];
	const wardOptions: IOption[] = [];

	const locationOptions: IOption[] = [
		{ value: "1", label: "DN" },
		{ value: "2", label: "SG" },
		{ value: "3", label: "HN" }
	];
	provinceData?.forEach(province => {
		const option: IOption = {
			value: province.id,
			label: province.name
		};
		provinceOptions.push(option);
	});
	districtData?.forEach(district => {
		const option: IOption = {
			value: district.id,
			label: district.name
		};
		districtOptions.push(option);
	});
	wardData?.forEach(ward => {
		const option: IOption = {
			value: ward.id,
			label: ward.name
		};
		wardOptions.push(option);
	});
	const handleInputChangeLocation = (current: string) => {
		if (current) {
			setIsCheckType(true);
		} else {
			setIsCheckType(false);
		}
	};

	const handleSubmit = async (values: IUserContactRegister, actions: FormikHelpers<IUserContactRegister>) => {
		registerWorker({
			userName: props.userLoginInfo.UserName.trim(),
			passWord: props.userLoginInfo.Password.trim(),
			email: props.userLoginInfo.Email.trim(),
			name: props.userPersonalInfo.Name.trim(),
			phone: values.PhoneNumber.trim(),
			identityCard: props.userPersonalInfo.IdentityCard.trim(),
			identityCardBy: props.userPersonalInfo.IdentityCardBy.trim(),
			identityCardDate: format(new Date(props.userPersonalInfo.IdentityCardDate), "dd-MM-yyyy"),
			gender: props.userPersonalInfo.Gender === "Nữ" ? "1" : props.userPersonalInfo.Gender === "Nam" ? "0" : "2",
			dateOfBirth: format(new Date(props.userPersonalInfo.DateOfBirth), "dd-MM-yyyy"),
			address: values.Address,
			addressPoint: "",
			startWorkingTime: props.userPersonalInfo.StartTime,
			endWorkingTime: props.userPersonalInfo.EndTime,
			provinceId: selectedProvince.value,
			provinceName: selectedProvince.label,
			districtId: selectedDistrict.value,
			districtName: selectedDistrict.label,
			wardId: selectedWard.value,
			wardName: selectedWard.label
		})
			.unwrap()
			.then(res => {
				console.log(JSON.stringify(res));
			})
			.then(() => {
				navigate("/");
			})
			.catch(error => {
				alert(JSON.stringify(error));
			});
	};
	const initialValues: IUserContactRegister = props.userContactInfo;
	return (
		<Formik
			initialValues={initialValues}
			validate={values => {
				const error: FormikErrors<IUserContactRegister> = {};
				if (!values.PhoneNumber) {
					error.PhoneNumber = "Phone number is required";
				}
				if (values.PhoneNumber && !/^[0-9\b]+$/.test(values.PhoneNumber.trim())) {
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
				if (!values.Ward) {
					error.Ward = "Ward is required";
				}
				return error;
			}}
			onSubmit={handleSubmit}
		>
			{formikProps => {
				const { values } = formikProps;
				return (
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
										options={provinceOptions}
										onChange={currentId => {
											console.log("currentId: ", currentId);

											setSelectedProvince(currentId);
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
										options={districtOptions}
										onChange={current => {
											setSelectedDistrict(current);
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
										name="Ward"
										options={wardOptions}
										onChange={current => {
											setSelectedWard(current);
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
										isDisabled={values !== userContactInfoEmpty ? false : true}
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
										onClick={() => {
											props.handleBackPage(2);
											props.handleGetContactInfo(values);
										}}
									>
										<GrCaretPrevious color="white" width="50px" height="50px" />
									</Flex>
								</HStack>
							</VStack>
						</Flex>
					</Form>
				);
			}}
		</Formik>
	);
};

export default WorkerContactInfo;
