import { Flex, VStack, HStack, Button } from "@chakra-ui/react";
import { Form, Formik, FormikErrors } from "formik";
import React, { useEffect, useState } from "react";
import { HiOutlineIdentification } from "react-icons/hi";
import { AiOutlinePhone, AiOutlineUser } from "react-icons/ai";
import { MdOutlineDateRange, MdPlace } from "react-icons/md";
import { CiCalendarDate } from "react-icons/ci";
import { GiPlayerTime } from "react-icons/gi";
import { TbAddressBook } from "react-icons/tb";
import { useDispatch, useSelector } from "react-redux";
import { format } from "date-fns";
import { useNavigate } from "react-router-dom";

import InputField from "../../common/shared-components/Input/InputField";
import { GenderType, PersonalInfoEmpty } from "../../common/constants/constants";
import SelectInputField from "../../common/shared-components/Select/SelectInputField";
import { IOption } from "../../common/shared-components/Select/SelectTypes";
import { useGetAllProvincesQuery } from "../../app/api/provinces/provinceApi";
import { useGetDistrictByIdProvinceQuery } from "../../app/api/districts/districtApi";
import { useGetWardByIdDistrictQuery } from "../../app/api/wards/wardApi";
import { getCurrentWorker, setCurrentWorker } from "../../app/reducer/loginAuth/loginAuthSlice";
import { useUpdateWorkerMutation } from "../../app/api/updateWorker/updateWorkerApi";
import { Popup } from "../../common/shared-components/Popup/Popup";
import { LoadingOverlay } from "../../common/shared-components/Loading/LoadingOverLay";

import { CheckBoxGroup } from "../CheckBoxGroup/CheckBoxGroup";

import { IPersonalInfoValue } from "./types";

export const PersonalInfo: React.FC = () => {
	const resultObj = useSelector(getCurrentWorker);
	const [isDisabledSave, setIsDisabledSave] = useState<boolean>(true);
	const [selectedProvince, setSelectedProvince] = useState<IOption>({ value: "", label: "" });
	const [selectedDistrict, setSelectedDistrict] = useState<IOption>({ value: "", label: "" });
	const [selectedWard, setSelectedWard] = useState<IOption>({ value: "", label: "" });
	const { data: provinceData } = useGetAllProvincesQuery();
	const { data: districtData } = useGetDistrictByIdProvinceQuery(selectedProvince.value);
	const { data: wardData } = useGetWardByIdDistrictQuery(selectedDistrict.value);
	const provinceOptions: IOption[] = [];
	const districtOptions: IOption[] = [];
	const wardOptions: IOption[] = [];
	const dispatch = useDispatch();
	const navigate = useNavigate();
	const [updateWorker, { isLoading: isUpdating }] = useUpdateWorkerMutation();

	const [isShowSuccessPopup, setIsShowSuccessPopup] = useState<boolean>(false);
	const [isShowErrorPopup, setIsShowErrorPopup] = useState<boolean>(false);
	const [messagePopup, setMessagePopup] = useState({ message: "", detail: "" });

	const [initialWorkerInfo, setInitialWorkerInfo] = useState<IPersonalInfoValue>(PersonalInfoEmpty);

	useEffect(() => {
		const dateBirth =
			resultObj.dateOfBirth && resultObj.dateOfBirth.includes("-")
				? resultObj.dateOfBirth.split("-")
				: resultObj.dateOfBirth && resultObj.dateOfBirth.includes("/")
				? resultObj.dateOfBirth.split("/")
				: "";
		const dateIdentityCard =
			resultObj.identityCardDate && resultObj.identityCardDate.includes("/")
				? resultObj.identityCardDate.split("/")
				: resultObj.identityCardDate && resultObj.identityCardDate.includes("-")
				? resultObj.identityCardDate.split("-")
				: "";
		setInitialWorkerInfo({
			name: resultObj.name,
			id: resultObj.id,
			gender: GenderType.find(item => item.id === resultObj.gender)?.type ?? "",
			address: resultObj.address,
			phone: String(resultObj.phone),
			identityCard: resultObj.identityCard,
			identityCardDate: resultObj.identityCardDate
				? `${dateIdentityCard[2]}-${dateIdentityCard[1]}-${dateIdentityCard[0]}`
				: "",
			identityCardBy: resultObj.identityCardBy,
			province: resultObj.provinceId,
			district: resultObj.districtId,
			ward: resultObj.wardId,
			dateOfBirth: resultObj.dateOfBirth ? `${dateBirth[2]}-${dateBirth[1]}-${dateBirth[0]}` : "",
			startWorkingTime: resultObj.startWorkingTime,
			endWorkingTime: resultObj.endWorkingTime,
			avatar: resultObj.avatar,
			addressPoint: resultObj.addressPoint
		});
		setSelectedDistrict({ value: resultObj.districtId, label: resultObj.districtName });
		setSelectedProvince({ value: resultObj.provinceId, label: resultObj.provinceName });
		setSelectedWard({ value: resultObj.wardId, label: resultObj.wardName });
	}, [dispatch, resultObj]);

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

	const handleSubmit = async (values: IPersonalInfoValue) => {
		const newWorkerInfo = new FormData();

		newWorkerInfo.append("name", values.name);
		newWorkerInfo.append("id", values.id);
		newWorkerInfo.append("gender", String(GenderType.find(item => item.type === values.gender)?.id ?? 0));
		newWorkerInfo.append("address", values.address);
		newWorkerInfo.append("phone", values.phone);
		newWorkerInfo.append("identityCard", values.identityCard);
		newWorkerInfo.append(
			"identityCardDate",
			format(new Date(values.identityCardDate ? values.identityCardDate : ""), "dd-MM-yyyy")
		);
		newWorkerInfo.append("identityCardBy", values.identityCardBy);
		newWorkerInfo.append("provinceId", selectedProvince.value);
		newWorkerInfo.append("provinceName", selectedProvince.label);
		newWorkerInfo.append("districtId", selectedDistrict.value);
		newWorkerInfo.append("districtName", selectedDistrict.label);
		newWorkerInfo.append("wardId", selectedWard.value);
		newWorkerInfo.append("wardName", selectedWard.label);
		newWorkerInfo.append("dateOfBirth", format(new Date(values.dateOfBirth ? values.dateOfBirth : ""), "dd-MM-yyyy"));
		newWorkerInfo.append("startWorkingTime", values.startWorkingTime);
		newWorkerInfo.append("endWorkingTime", values.endWorkingTime);
		newWorkerInfo.append("avatar", values.avatar);
		newWorkerInfo.append("addressPoint", "");

		updateWorker({ workerId: values.id, formdata: newWorkerInfo })
			.unwrap()
			.then(res => {
				console.log("success");

				if (res.isSuccessed) {
					localStorage.setItem("user", JSON.stringify(res.resultObj));
					dispatch(setCurrentWorker(res.resultObj));
					setMessagePopup({
						...messagePopup,
						message: "Thành công",
						detail: "Bạn đã thay đổi thông tin thành công"
					});
					setIsShowSuccessPopup(true);
				} else {
					setMessagePopup({
						...messagePopup,
						message: "Xảy ra lỗi",
						detail: res.message
					});
					setIsShowErrorPopup(true);
				}
			})
			.catch(error => {
				console.log("error: ", error);

				setMessagePopup({
					...messagePopup,
					message: `Xảy ra lỗi ${error.status}`,
					detail: error.error
				});
				setIsShowErrorPopup(true);
			});
	};

	const initialValues: IPersonalInfoValue = initialWorkerInfo;

	return (
		<Formik
			initialValues={initialValues}
			enableReinitialize
			validate={values => {
				const errors: FormikErrors<IPersonalInfoValue> = {};

				if (!values.name) {
					errors.name = "Please fill out your name";
				}
				if (!values.identityCard) {
					errors.identityCard = "Identity card is required";
				}
				if (values.identityCard && !/^[0-9\b]+$/.test(values.identityCard.trim())) {
					errors.identityCard = "Identity card must be number";
				}
				if (!values.identityCardDate) {
					errors.identityCardDate = "Identity card date is required";
				}
				if (!values.identityCardBy) {
					errors.identityCardBy = "Identity card by is required";
				}
				if (!values.gender) {
					errors.gender = "Gender is required";
				}
				if (!values.dateOfBirth) {
					errors.dateOfBirth = "Date of birth is required";
				}
				if (!values.startWorkingTime) {
					errors.startWorkingTime = "Start time is required";
				}
				if (!values.endWorkingTime) {
					errors.endWorkingTime = "End time is required";
				}
				if (!values.phone) {
					errors.phone = "Phone number is required";
				}
				if (values.phone && !/^[0-9\b]+$/.test(values.phone.trim())) {
					errors.phone = "Phone Number must be number";
				}
				if (!values.address) {
					errors.address = "Address is required";
				}
				if (!values.province) {
					errors.province = "Province is required";
				}
				if (!values.district) {
					errors.district = "District is required";
				}
				if (!values.ward) {
					errors.ward = "Ward is required";
				}
				if (
					values.name &&
					values.identityCard &&
					values.identityCardBy &&
					values.identityCardDate &&
					values.gender &&
					values.dateOfBirth &&
					values.startWorkingTime &&
					values.endWorkingTime &&
					values.phone &&
					values.address &&
					values.ward &&
					values.province &&
					values.district
				) {
					setIsDisabledSave(false);
				} else {
					setIsDisabledSave(true);
				}
				return errors;
			}}
			onSubmit={handleSubmit}
		>
			{() => {
				return (
					<Form>
						<Flex width={"100%"} height={"100%"} justifyContent={"center"} alignContent="center">
							<VStack
								width={"100%"}
								height={"100%"}
								justifyContent={"center"}
								alignContent="center"
								boxSizing="border-box"
								spacing={4}
								overflow="scroll"
								padding={"0px 20px"}
							>
								<InputField
									name="name"
									label="Họ và tên"
									inputProps={{
										fontSize: "18px",
										height: "45px",
										fontWeight: 400,
										width: "100%",
										color: "rgba(0, 0, 0)",
										paddingRight: "5px"
									}}
									childrenIcon={<AiOutlineUser />}
								/>
								<InputField
									name="identityCard"
									label="CCCD/CMND"
									inputProps={{
										fontSize: "18px",
										height: "45px",
										fontWeight: 400,
										width: "100%",
										color: "rgba(0, 0, 0)",
										maxLength: 12
									}}
									childrenIcon={<HiOutlineIdentification size={"20px"} />}
								/>
								<HStack spacing={15} width={"100%"} justifyContent="space-between">
									<InputField
										name="identityCardDate"
										label="Ngày cấp"
										inputProps={{
											fontSize: "18px",
											height: "45px",
											fontWeight: 400,
											width: "100%",
											color: "rgba(0, 0, 0)",
											type: "date",
											placeholder: "DD-MM-YYYY"
										}}
										childrenIcon={<MdOutlineDateRange />}
									/>
									<InputField
										name="identityCardBy"
										label="Nơi cấp"
										inputProps={{
											fontSize: "18px",
											height: "45px",
											fontWeight: 400,
											width: "100%",
											color: "rgba(0, 0, 0)"
										}}
										childrenIcon={<MdPlace />}
									/>
								</HStack>
								<HStack spacing={3} width={"100%"}>
									<CheckBoxGroup name="gender" label="Giới tính" valueProps={""} />
									<InputField
										name="dateOfBirth"
										label="Ngày sinh"
										inputProps={{
											fontSize: "18px",
											height: "45px",
											fontWeight: 400,
											width: "100%",
											color: "rgba(0, 0, 0)",
											type: "date",
											placeholder: "DD-MM-YYYY"
										}}
										childrenIcon={<CiCalendarDate />}
									/>
								</HStack>

								<HStack spacing={8} width={"100%"} paddingBottom="5px">
									<InputField
										name="startWorkingTime"
										label="Thời gian bắt đầu làm việc"
										inputProps={{
											type: "time",
											fontSize: "18px",
											height: "45px",
											fontWeight: 400,
											width: "100%",
											color: "rgba(0, 0, 0)"
										}}
										childrenIcon={<GiPlayerTime />}
									/>
									<InputField
										name="endWorkingTime"
										label="Thời gian kết thúc làm việc"
										inputProps={{
											type: "time",
											fontSize: "18px",
											height: "45px",
											fontWeight: 400,
											width: "100%",
											color: "rgba(0, 0, 0)"
										}}
										childrenIcon={<GiPlayerTime />}
									/>
								</HStack>
								<InputField
									name="phone"
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
										name="province"
										options={provinceOptions}
										onChange={currentId => {
											console.log("currentId: ", currentId);

											setSelectedProvince(currentId);
										}}
										style={{ height: "45px" }}
									/>
									<SelectInputField
										width="100%"
										label="Quận"
										placeholder="Chọn quận"
										name="district"
										options={districtOptions}
										onChange={current => {
											setSelectedDistrict(current);
										}}
										style={{ height: "45px" }}
									/>
								</HStack>
								<HStack spacing={6} width={"100%"}>
									<SelectInputField
										width="100%"
										label="Huyện"
										placeholder="Chọn huyện"
										name="ward"
										options={wardOptions}
										onChange={current => {
											setSelectedWard(current);
										}}
										style={{ height: "45px" }}
									/>
									<InputField
										name="address"
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
								<HStack
									height={"100%"}
									width="100%"
									position={"relative"}
									bottom="0"
									alignContent={"center"}
									justifyContent={"center"}
									padding="50px 0px"
								>
									<Button
										width="20%"
										height={"50px"}
										borderRadius="15px"
										_hover={{
											backGroundColor: "#fc5352"
										}}
										fontSize="20px"
										onClick={() => navigate("/job-manage")}
									>
										Huỷ bỏ
									</Button>
									<Button
										width="20%"
										height={"50px"}
										bgColor={"#f9d475"}
										borderRadius="15px"
										color={"#000000"}
										_hover={{
											backGroundColor: "#fc5352",
											boxShadow: "0px 0px 10px 3px #f9d475"
										}}
										fontSize="20px"
										type="submit"
										isDisabled={isDisabledSave}
										cursor="pointer"
									>
										Lưu thay đổi
									</Button>
								</HStack>
							</VStack>
						</Flex>
						{isShowSuccessPopup && (
							<Popup
								typeAlert="success"
								openAlert={isShowSuccessPopup}
								message={messagePopup.message}
								details={messagePopup.detail}
								handleCloseAlert={(isShowPopup: boolean) => {
									setIsShowSuccessPopup(isShowPopup);
									navigate("/job-manage");
								}}
							/>
						)}
						{isShowErrorPopup && (
							<Popup
								typeAlert="error"
								openAlert={isShowErrorPopup}
								message={messagePopup.message}
								details={messagePopup.detail}
								handleCloseAlert={(isShowPopup: boolean) => setIsShowErrorPopup(isShowPopup)}
							/>
						)}
						{isUpdating && <LoadingOverlay openLoading={isUpdating} />}
					</Form>
				);
			}}
		</Formik>
	);
};
