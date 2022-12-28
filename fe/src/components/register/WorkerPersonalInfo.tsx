import { Flex, VStack, Text, HStack, Button } from "@chakra-ui/react";
import { Form, Formik, FormikErrors } from "formik";
import React, { useEffect, useState } from "react";
import { HiOutlineIdentification } from "react-icons/hi";
import { BsPersonSquare } from "react-icons/bs";
import { GrCaretNext, GrCaretPrevious } from "react-icons/gr";
import { AiOutlineUser } from "react-icons/ai";
import { MdOutlineDateRange, MdPlace } from "react-icons/md";
import { CiCalendarDate } from "react-icons/ci";
import { GiPlayerTime } from "react-icons/gi";

import InputField from "../../common/shared-components/Input/InputField";
import { IUserPersonalRegister } from "../../app/api/register/registerType";
import { userPersonalInfoEmpty } from "../../common/constants/constants";

import { CheckBoxGroup } from "../CheckBoxGroup/CheckBoxGroup";

interface IWorkerPersonalInfo {
	handleNextPage: (page: number) => void;
	handleBackPage: (page: number) => void;
	handleGetPersonalInfo: (values: IUserPersonalRegister) => void;
	userPersonalInfo: IUserPersonalRegister;
}

const WorkerPersonalInfo: React.FC<IWorkerPersonalInfo> = props => {
	const [isDisabledNext, setIsDisabledNext] = useState<boolean>(true);

	useEffect(() => {
		if (props.userPersonalInfo === userPersonalInfoEmpty) {
			setIsDisabledNext(true);
		} else {
			setIsDisabledNext(false);
		}
	}, [props.userPersonalInfo]);

	const handleSubmit = async (values: IUserPersonalRegister) => {
		props.handleGetPersonalInfo(values);
		props.handleNextPage(3);
	};

	const initialValues: IUserPersonalRegister = props.userPersonalInfo;

	return (
		<Formik
			initialValues={initialValues}
			validate={values => {
				const errors: FormikErrors<IUserPersonalRegister> = {};

				if (!values.Name) {
					errors.Name = "Vui lòng nhập họ và tên";
				}
				if (!values.IdentityCard) {
					errors.IdentityCard = "Vui lòng nhập CCCD/CMND";
				}
				if (values.IdentityCard && !/^[0-9\b]+$/.test(values.IdentityCard.trim())) {
					errors.IdentityCard = "Định dạng CCCD/CMND nên là chữ số";
				}
				if (!values.IdentityCardDate) {
					errors.IdentityCardDate = "Vui lòng nhập ngày cấp";
				}
				if (!values.IdentityCardBy) {
					errors.IdentityCardBy = "Vui lòng nhập nơi cấp";
				}
				if (!values.DateOfBirth) {
					errors.DateOfBirth = "Vui lòng nhập ngày sinh";
				}
				if (!values.StartTime) {
					errors.StartTime = "Vui lòng nhập thời gian bắt đầu công việc";
				}
				if (!values.EndTime) {
					errors.EndTime = "Vui lòng nhập thời gian kết thúc công việc";
				}
				if (
					values.Name &&
					values.IdentityCard &&
					values.IdentityCardBy &&
					values.IdentityCardDate &&
					values.Gender &&
					values.DateOfBirth &&
					values.StartTime &&
					values.EndTime
				) {
					setIsDisabledNext(false);
				} else {
					setIsDisabledNext(true);
				}
				return errors;
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
								overflow="scroll"
							>
								<HStack justifyContent={"flex-start"} width="100%" spacing={4}>
									<BsPersonSquare color="#f9d475" size={"30px"} />
									<Text
										fontFamily={"'Bungee', monospace"}
										fontWeight="700"
										fontSize={{ sm: "4.4vw", md: "30px", lg: "50px" }}
										bgClip="text"
										color={"#f9d475"}
									>
										Thông tin cá nhân
									</Text>
								</HStack>
								<InputField
									name="Name"
									label="Họ và tên"
									inputProps={{
										fontSize: "18px",
										height: "40px",
										fontWeight: 400,
										width: "100%",
										color: "rgba(0, 0, 0)",
										paddingRight: "5px"
									}}
									childrenIcon={<AiOutlineUser />}
								/>
								<InputField
									name="IdentityCard"
									label="CCCD/CMND"
									inputProps={{
										fontSize: "18px",
										height: "40px",
										fontWeight: 400,
										width: "100%",
										color: "rgba(0, 0, 0)",
										maxLength: 12
									}}
									childrenIcon={<HiOutlineIdentification size={"20px"} />}
								/>
								<HStack spacing={15} width={"100%"} justifyContent="space-between">
									<InputField
										name="IdentityCardDate"
										label="Ngày cấp"
										inputProps={{
											fontSize: "18px",
											height: "40px",
											fontWeight: 400,
											width: "100%",
											color: "rgba(0, 0, 0)",
											type: "date",
											placeholder: "DD-MM-YYYY"
										}}
										childrenIcon={<MdOutlineDateRange />}
									/>
									<InputField
										name="IdentityCardBy"
										label="Nơi cấp"
										inputProps={{
											fontSize: "18px",
											height: "40px",
											fontWeight: 400,
											width: "100%",
											color: "rgba(0, 0, 0)"
										}}
										childrenIcon={<MdPlace />}
									/>
								</HStack>
								<CheckBoxGroup name="Gender" label="Giới tính" valueProps={props.userPersonalInfo.Gender} />
								<InputField
									name="DateOfBirth"
									label="Ngày sinh"
									inputProps={{
										fontSize: "18px",
										height: "40px",
										fontWeight: 400,
										width: "100%",
										color: "rgba(0, 0, 0)",
										type: "date"
									}}
									childrenIcon={<CiCalendarDate />}
								/>

								<HStack spacing={8} width={"100%"} paddingBottom="5px">
									<InputField
										name="StartTime"
										label="Thời gian bắt đầu làm việc"
										inputProps={{
											type: "time",
											fontSize: "18px",
											height: "40px",
											fontWeight: 400,
											width: "100%",
											color: "rgba(0, 0, 0)"
										}}
										childrenIcon={<GiPlayerTime />}
									/>
									<InputField
										name="EndTime"
										label="Thời gian kết thúc làm việc"
										inputProps={{
											type: "time",
											fontSize: "18px",
											height: "40px",
											fontWeight: 400,
											width: "100%",
											color: "rgba(0, 0, 0)"
										}}
										childrenIcon={<GiPlayerTime />}
									/>
								</HStack>
								<HStack justifyContent={"space-between"} width="100%">
									<Flex
										bgColor={"#f9d475"}
										width="50px"
										height="50px"
										borderRadius={"30px"}
										alignItems="center"
										justifyContent={"center"}
										cursor="pointer"
										boxShadow="8px 8px 10px rgb(249, 212, 117, 0.6)"
										_hover={{
											bgColor: "#FFCC33"
										}}
										onClick={() => {
											props.handleBackPage(1);
											props.handleGetPersonalInfo(values);
										}}
									>
										<GrCaretPrevious color="#000000" width="50px" height="50px" />
									</Flex>
									<Button
										bgColor={"#f9d475"}
										width="50px"
										height="50px"
										borderRadius={"30px"}
										alignItems="center"
										justifyContent={"center"}
										cursor="pointer"
										boxShadow="8px 8px 10px rgb(249, 212, 117, 0.6)"
										_hover={{
											bgColor: "#FFCC33"
										}}
										type="submit"
										isDisabled={isDisabledNext}
									>
										<GrCaretNext color="#000000" width="50px" height="50px" />
									</Button>
								</HStack>
							</VStack>
						</Flex>
					</Form>
				);
			}}
		</Formik>
	);
};

export default WorkerPersonalInfo;
