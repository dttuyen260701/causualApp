import { Box, Flex, VStack, Text, HStack, Link, Button } from "@chakra-ui/react";
import { Form, Formik, FormikErrors } from "formik";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { FaRegUser } from "react-icons/fa";
import { HiOutlineMail } from "react-icons/hi";
import { RiLockPasswordLine, RiLockPasswordFill } from "react-icons/ri";
import { GrCaretNext } from "react-icons/gr";

import InputField from "../../common/shared-components/Input/InputField";
import { IUserLoginRegister } from "../../app/api/register/registerType";
import { userLoginInfoEmpty } from "../../common/constants/constants";

interface ILoginInfo {
	handleNextPage: (page: number) => void;
	handleGetLoginInfo: (values: IUserLoginRegister) => void;
	userLoginInfo: IUserLoginRegister;
}

const LoginInfo: React.FC<ILoginInfo> = (props: ILoginInfo) => {
	const navigate = useNavigate();
	const [isDisableNext, setIsDisableNext] = useState<boolean>(true);

	useEffect(() => {
		if (props.userLoginInfo === userLoginInfoEmpty) {
			setIsDisableNext(true);
		} else {
			setIsDisableNext(false);
		}
	}, [props.userLoginInfo]);

	const handleSubmit = async (values: IUserLoginRegister) => {
		props.handleGetLoginInfo(values);
		props.handleNextPage(2);
	};
	const initialValues: IUserLoginRegister = props.userLoginInfo;
	return (
		<Formik
			initialValues={initialValues}
			validate={values => {
				const error: FormikErrors<IUserLoginRegister> = {};
				if (!values.UserName) {
					error.UserName = "Vui lòng nhập tên đăng nhập";
				}
				if (!values.Password) {
					error.Password = "Vui lòng nhập mật khẩu";
				}
				if (
					values.Password &&
					!/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,})/.test(values.Password.trim())
				) {
					error.Password =
						"Mật khẩu nên chứa ít nhất 8 kí tự, ít nhất một kí tự viết hoa, một kí tự thường, một sô và một ký tự đặc biệt";
				}
				if (!values.confirmPassword) {
					error.confirmPassword = "Vui lòng xác nhận mật khẩu";
				}
				if (values.confirmPassword !== values.Password) {
					error.confirmPassword = "Xác nhận mật khẩu chưa chính xác";
				}
				if (!values.Email) {
					error.Email = "Vui lòng nhập email";
				}
				if (values.Email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(values.Email.trim())) {
					error.Email = "Định dạng email chưa chính xác";
				}
				if (values.UserName && values.Email && values.Password && values.confirmPassword) {
					setIsDisableNext(false);
				} else {
					setIsDisableNext(true);
				}
				return error;
			}}
			onSubmit={handleSubmit}
			validateOnChange={true}
		>
			{() => (
				<Form>
					<Flex width={"100%"} height={"100%"} bgColor={"white"} justifyContent={"center"} marginTop={"5px"}>
						<VStack width={"100%"} height={"100%"} justifyContent={"center"} spacing={3}>
							<HStack width={"100%"} justifyContent={"space-between"} spacing={20}>
								<Text
									fontSize="16px"
									fontWeight="bold"
									bgClip="text"
									bgGradient="linear-gradient(to right, #d05d9c 0%,#f8bb1c 100%);"
								>
									Casual Helper
								</Text>
								<Text>
									Bạn đã có tài khoản?{" "}
									<Link
										color="#f9d475"
										textDecoration={"underline"}
										onClick={() => {
											navigate("/login");
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
									fontWeight="500"
									fontSize={{ sm: "5.5vw", md: "40px", lg: "50px" }}
									bgClip="text"
									color={"#f9d475"}
								>
									Trở thành đối tác
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
							<HStack spacing={6} width={"100%"} height="auto" paddingBottom="30px">
								{/* Password */}
								<InputField
									name="Password"
									label="Mật khẩu"
									inputProps={{
										fontSize: "20px",
										height: "50px",
										fontWeight: "400",
										width: "100%",
										color: "rgba(0, 0, 0)",
										type: "password"
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
										color: "rgba(0, 0, 0)",
										type: "password"
									}}
									childrenIcon={<RiLockPasswordFill />}
								/>
							</HStack>
							<HStack justifyContent={"flex-end"} width="100%">
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
										bgColor: "#f9d475"
									}}
									type="submit"
									isDisabled={isDisableNext}
								>
									<GrCaretNext color="#000000" width="50px" height="50px" />
								</Button>
							</HStack>
						</VStack>
					</Flex>
				</Form>
			)}
		</Formik>
	);
};

export default LoginInfo;
