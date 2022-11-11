import { Box, Flex, VStack, Text, HStack, Link } from "@chakra-ui/react";
import { Form, Formik, FormikErrors, FormikHelpers } from "formik";
import React from "react";
import { useNavigate } from "react-router-dom";
import { FaRegUser } from "react-icons/fa";
import { HiOutlineMail } from "react-icons/hi";
import { RiLockPasswordLine, RiLockPasswordFill } from "react-icons/ri";
import { GrCaretNext } from "react-icons/gr";

import InputField from "../../common/shared-components/Input/InputField";
import { IUserLoginRegister } from "../../app/api/register/registerType";

interface ILoginInfo {
	handleNextPage: (page: number) => void;
}

const LoginInfo: React.FC<ILoginInfo> = (props: ILoginInfo) => {
	const navigate = useNavigate();

	const handleTurnPage = () => {
		props.handleNextPage(2);
	};
	const handleSubmit = async (values: IUserLoginRegister, actions: FormikHelpers<IUserLoginRegister>) => {
		alert(`Username: ${values.UserName}\nEmail: ${values.Email}\nPassword: ${values.Password}`);
	};
	const initialValues: IUserLoginRegister = {
		UserName: "",
		Password: "",
		confirmPassword: "",
		Email: ""
	};
	return (
		<Formik
			initialValues={initialValues}
			validate={values => {
				const error: FormikErrors<IUserLoginRegister> = {};
				if (!values.UserName) {
					error.UserName = "UserName is required";
				}
				if (!values.Password) {
					error.Password = "Password is required";
				}
				if (!values.confirmPassword) {
					error.confirmPassword = "Password has to be confirmed";
				}
				if (!values.Email) {
					error.Email = "Email is required";
				}
				if (values.Email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(values.Email.trim())) {
					error.Email = "Email address is invalid";
				}
				return error;
			}}
			onSubmit={handleSubmit}
		>
			{() => (
				<Form>
					<Flex width={"100%"} height={"100%"} bgColor={"white"} justifyContent={"center"} marginTop={"5px"}>
						<VStack
							//position={"relative"}
							width={"100%"}
							height={"100%"}
							justifyContent={"center"}
							spacing={3}
						>
							<HStack width={"100%"} justifyContent={"space-between"} spacing={20}>
								<Text
									fontSize="16px"
									fontWeight="bold"
									bgClip="text"
									bgGradient="linear-gradient(to right, #d05d9c 0%,#f8bb1c 100%);"
									//position="absolute"
									//bottom="16px"
								>
									Casual Manager
								</Text>
								<Text>
									Bạn đã có tài khoản?{" "}
									<Link
										color="#E48D41"
										textDecoration={"underline"}
										onClick={() => {
											navigate("/");
										}}
									>
										Đăng nhập
									</Link>
								</Text>
							</HStack>
							{/* Heading */}
							<Box padding={"20px 0 10px"}>
								<Text
									fontFamily={"'Bungee', monospace"}
									fontWeight="700"
									fontSize={{ sm: "5.5vw", md: "60px", lg: "70px" }}
									bgClip="text"
									color={"#E48D41"}
								>
									Đăng ký
								</Text>
							</Box>
							<HStack spacing={6} width={"100%"}>
								{/*Username*/}
								<InputField
									name="UserName"
									label="Tên đăng nhập"
									inputProps={{
										fontSize: "20px",
										height: "50px",
										fontWeight: 400,
										width: "100%",
										color: "rgba(0, 0, 0)"
									}}
									childrenIcon={<FaRegUser />}
								/>
								{/* Email */}
								<InputField
									name="Email"
									label="Email"
									inputProps={{
										//type: 'email',
										fontSize: "20px",
										height: "50px",
										fontWeight: 400,
										width: "100%",
										color: "rgba(0, 0, 0)"
									}}
									childrenIcon={<HiOutlineMail />}
								/>
							</HStack>
							<HStack spacing={6} width={"100%"} paddingBottom="30px">
								{/* Password */}
								<InputField
									name="Password"
									label="Mật khẩu"
									inputProps={{
										fontSize: "20px",
										height: "50px",
										fontWeight: 400,
										width: "100%",
										type: "password",
										color: "rgba(0, 0, 0)"
									}}
									childrenIcon={<RiLockPasswordLine />}
								/>
								{/* Confirm Password */}
								<InputField
									name="confirmPassword"
									label="Xác nhận mật khẩu"
									inputProps={{
										fontSize: "20px",
										height: "50px",
										fontWeight: 400,
										width: "100%",
										type: "password",
										color: "rgba(0, 0, 0)"
									}}
									childrenIcon={<RiLockPasswordFill />}
								/>
							</HStack>
							<HStack justifyContent={"flex-end"} width="100%">
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
									onClick={handleTurnPage}
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

export default LoginInfo;
