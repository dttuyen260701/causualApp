import { Flex, VStack, Text, HStack } from "@chakra-ui/react";
import { Form, Formik, FormikErrors, FormikHelpers } from "formik";
import React from "react";
import { HiOutlineIdentification } from "react-icons/hi";
import { BsPersonSquare } from "react-icons/bs";
import { GrCaretNext, GrCaretPrevious } from "react-icons/gr";
import { AiOutlineUser } from "react-icons/ai";
import { MdOutlineDateRange, MdPlace } from "react-icons/md";
import { CiCalendarDate } from "react-icons/ci";
import { GiPlayerTime } from "react-icons/gi";

import InputField from "../../common/shared-components/Input/InputField";
import { IUserPersonalRegister } from "../../app/api/register/registerType";

import { CheckBoxGroup } from "../CheckBoxGroup/CheckBoxGroup";

interface IWorkerPersonalInfo {
	handleNextPage: (page: number) => void;
	handleBackPage: (page: number) => void;
}

const WorkerPersonalInfo: React.FC<IWorkerPersonalInfo> = props => {
	const handleNextPage = () => {
		props.handleNextPage(3);
	};
	const handleBackPage = () => {
		props.handleBackPage(1);
	};
	const handleSubmit = async (values: IUserPersonalRegister, actions: FormikHelpers<IUserPersonalRegister>) => {
		alert(values);
	};
	const initialValues: IUserPersonalRegister = {
		Name: "",
		IdentityCard: "",
		IdentityCardDate: "",
		IdentityCardBy: "",
		Gender: "",
		DateOfBirth: "",
		StartTime: "",
		EndTime: ""
	};
	return (
		<Formik
			initialValues={initialValues}
			validate={values => {
				const error: FormikErrors<IUserPersonalRegister> = {};
				if (!values.Name) {
					error.Name = "Please fill out your name";
				}
				if (!values.IdentityCard) {
					error.IdentityCard = "Identity card is required";
				}
				if (values.IdentityCard && !/^\d{10}$/.test(values.IdentityCard.trim())) {
					error.IdentityCard = "Identity card must be number";
				}
				if (!values.IdentityCardDate) {
					error.IdentityCardDate = "Identity card date is required";
				}
				if (!values.IdentityCardBy) {
					error.IdentityCardBy = "Identity card by is required";
				}

				if (!values.Gender) {
					error.Gender = "Gender is required";
				}
				if (!values.DateOfBirth) {
					error.DateOfBirth = "Date of birth is required";
				}
				if (!values.StartTime) {
					console.log("validate starttime");

					error.StartTime = "Start time is required";
				}
				if (!values.EndTime) {
					error.EndTime = "End time is required";
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
							<HStack justifyContent={"flex-start"} width="100%" spacing={4}>
								<BsPersonSquare color="#E48D41" size={"30px"} />
								<Text
									fontFamily={"'Bungee', monospace"}
									fontWeight="700"
									fontSize={{ sm: "4.4vw", md: "30px", lg: "50px" }}
									bgClip="text"
									color={"#E48D41"}
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
										type: "date"
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
							<HStack spacing={6} width={"100%"}>
								<CheckBoxGroup name="Gender" label="Giới tính" />
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
							</HStack>

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
									onClick={handleNextPage}
								>
									<GrCaretNext color="white" width="50px" height="50px" />
								</Flex>
							</HStack>
						</VStack>
					</Flex>
				</Form>
			)}
		</Formik>
	);
};

export default WorkerPersonalInfo;
