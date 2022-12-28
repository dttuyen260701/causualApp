import {
	Box,
	Button,
	Flex,
	FormControl,
	FormErrorMessage,
	HStack,
	Icon,
	Img,
	Input,
	InputGroup,
	InputRightElement,
	VStack,
	Text,
	useMediaQuery,
	FormLabel,
	Link
} from "@chakra-ui/react";
import { Field, Form, Formik, FormikErrors, FormikHelpers, FieldProps } from "formik";
import React, { useState } from "react";
import { AiFillEye, AiFillEyeInvisible } from "react-icons/ai";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";

import { useLoginMutation } from "../app/api/login/loginApi";
import { IUserLoginInput } from "../app/api/login/loginType";
import { setCurrentWorker } from "../app/reducer/loginAuth/loginAuthSlice";
import { LoadingOverlay } from "../common/shared-components/Loading/LoadingOverLay";
import { Popup } from "../common/shared-components/Popup/Popup";

const LoginPage: React.FC = () => {
	const [showRightPanel] = useMediaQuery("(min-width: 1100px)");
	const [showPassword, setShowPassword] = useState(false);
	const [loginWorker, { isLoading }] = useLoginMutation();
	const [errorState, setErrorState] = useState(false);
	const dispatch = useDispatch();
	const navigate = useNavigate();
	const [isShowErrorPopup, setIsShowErrorPopup] = useState<boolean>(false);
	const [messagePopup, setMessagePopup] = useState({ message: "", detail: "" });

	const handleShowClick = () => {
		setShowPassword(!showPassword);
	};

	const handleSubmit = async (values: IUserLoginInput, actions: FormikHelpers<IUserLoginInput>) => {
		loginWorker({
			userName: values.userName.trim(),
			password: values.password,
			rememberMe: values.rememberMe,
			withRole: values.withRole
		})
			.unwrap()
			.then(res => {
				localStorage.setItem("user", JSON.stringify(res.resultObj));
				if (res.isSuccessed) {
					dispatch(setCurrentWorker(res.resultObj));
					navigate("/job-manage");
				} else {
					setMessagePopup({
						...messagePopup,
						message: "Xảy ra lỗi",
						detail: res.message
					});
					setIsShowErrorPopup(true);
					actions.resetForm({ values: { userName: values.userName, password: "", rememberMe: true, withRole: 1 } });
				}
			})
			.catch(error => {
				setMessagePopup({
					...messagePopup,
					message: "Xảy ra lỗi",
					detail: error.error
				});
				setIsShowErrorPopup(true);
				actions.resetForm({ values: { userName: values.userName, password: "", rememberMe: true, withRole: 1 } });
				setErrorState(true);
			});
	};

	const initialValues: IUserLoginInput = {
		userName: "",
		password: "",
		rememberMe: true,
		withRole: 1
	};

	return (
		<Formik
			initialValues={initialValues}
			validate={values => {
				const error: FormikErrors<IUserLoginInput> = {};
				if (!values.userName) {
					error.userName = "Email address is required";
				}
				if (!values.password) {
					error.password = "Password is required";
				}
				if (errorState) {
					setErrorState(false);
				}
				return error;
			}}
			onSubmit={(values, actions) => {
				handleSubmit(values, actions);
			}}
		>
			{() => (
				<Form>
					<Flex
						flexDirection="column"
						width="100wh"
						height="100vh"
						backgroundColor="#faf2e5"
						justifyContent="center"
						alignItems="center"
						position="relative"
					>
						<HStack
							w={{ sm: "100%", smd: "85%", md: "70%" }}
							h={{ sm: "100%", smd: "90%", md: "80%" }}
							width={"60vw"}
							height={"70vh"}
							bgColor="#f9d475"
							spacing={0}
							boxShadow="8px 8px 10px rgb(249, 212, 117, 0.6)"
						>
							{/* Login-Input */}
							<VStack
								position="relative"
								spacing={4}
								width="100%"
								height="100%"
								bgColor={"white"}
								justifyContent={"center"}
							>
								<Box paddingBottom="20px">
									<Text
										fontFamily={"'Bungee', monospace"}
										fontWeight="600"
										fontSize={{ sm: "5.0vw", md: "50px", lg: "55px" }}
										bgClip="text"
										color={"#f9d475"}
										pb={showRightPanel ? "20px" : "0px"}
									>
										ĐĂNG NHẬP
									</Text>
								</Box>
								{/* Email */}
								<Field name="userName">
									{({ field, form }: FieldProps) => (
										<FormControl
											isInvalid={!!form.errors[field.name] && !!form.touched[field.name]}
											width={{ sm: "72%", md: "205px", lg: "270px" }}
											variant="floating"
										>
											<Input
												maxLength={50}
												placeholder=" "
												{...field}
												autoFocus
												color={"rgba(0, 0, 0)"}
												fontSize={{ sm: "14px", smd: "16px" }}
												_hover={{
													shadow: "md",
													borderColor: "#f9d475",
													borderWidth: "2px",
													boxShadow: "0px 0px 13px -3px #FDB493"
												}}
											/>
											<FormLabel color={"rgba(88, 88, 94, 0.6)"} fontSize={{ sm: "14px", smd: "16px" }}>
												Tên đăng nhập
											</FormLabel>
											{form.errors[field.name] && (
												<FormErrorMessage>{form.errors[field.name] as string}</FormErrorMessage>
											)}
										</FormControl>
									)}
								</Field>

								{/* Password */}
								<Field name="password">
									{({ field, form }: FieldProps) => (
										<FormControl
											isInvalid={!!form.errors[field.name] && !!form.touched[field.name]}
											width={{ sm: "72%", md: "205px", lg: "270px" }}
											variant="floating"
										>
											<InputGroup>
												<Input
													placeholder=" "
													_placeholder={{ color: "secondary" }}
													{...field}
													type={showPassword ? "text" : "password"}
													color={"rgba(0, 0, 0)"}
													fontSize={{ sm: "14px", smd: "16px" }}
													_hover={{
														shadow: "md",
														borderColor: "#f9d475",
														borderWidth: "2px",
														boxShadow: "0px 0px 13px -3px #FDB493"
													}}
												/>
												<FormLabel color={"rgba(88, 88, 94, 0.6)"} fontSize={{ sm: "14px", smd: "16px" }}>
													Mật khẩu
												</FormLabel>
												<InputRightElement width="2.5rem">
													<Button h="20px" w="8px" size="sm" onClick={handleShowClick}>
														{showPassword ? (
															<Icon as={AiFillEye} fontSize="16px" color={"black"} />
														) : (
															<Icon as={AiFillEyeInvisible} fontSize="16px" color={"black"} />
														)}
													</Button>
												</InputRightElement>
											</InputGroup>
											{form.errors[field.name] && (
												<FormErrorMessage>{form.errors[field.name] as string}</FormErrorMessage>
											)}
											{errorState ? (
												<Box color={"red"} marginTop={"5px"} fontSize={14}>
													Wrong email address or password{" "}
												</Box>
											) : null}
										</FormControl>
									)}
								</Field>
								{/* Button Login */}
								<Button
									borderRadius={5}
									type="submit"
									bgColor="#f9d475"
									color={"rgba(88, 88, 94, 0.8)"}
									width={{ sm: "72%", md: "205px", lg: "270px" }}
									_hover={{
										transform: "translateY(-10%)",
										transition: "0.2s all linear",
										shadow: "0px 8px 13px -3px #f9d475"
									}}
									fontSize={{ sm: "14px", smd: "16px" }}
								>
									Đăng nhập
								</Button>
								{/* Register */}
								<HStack justifyContent={"flex-end"}>
									<Text color={"rgba(88, 88, 94, 0.8)"} fontWeight="400">
										Bạn chưa có tài khoản?
									</Text>
									<Link
										color={"#f9d475"}
										textDecoration="underline"
										onClick={() => {
											navigate("/register");
										}}
									>
										Đăng ký
									</Link>
								</HStack>
								{!showRightPanel && (
									<Text
										fontSize="16px"
										fontWeight="bold"
										bgClip="text"
										bgGradient="linear-gradient(to right, #d05d9c 0%,#f8bb1c 100%);"
										position="absolute"
										bottom="16px"
									>
										Casual Manager
									</Text>
								)}
							</VStack>
							{/* Logo */}
							{showRightPanel && (
								<VStack
									width="100%"
									height="100%"
									bgColor="#f9d475"
									justify="center"
									align="center"
									position="relative"
								>
									<HStack justifyContent={"center"} align={"center"}>
										<Img
											w={{ md: "230px", lg: "300px" }}
											h={{ md: "230px", lg: "300px" }}
											src="/images/Login-removeBg.png"
										/>
									</HStack>
									<Text
										fontSize="18px"
										fontWeight="500"
										bgClip="text"
										color={"#000000"}
										position="absolute"
										bottom="16px"
									>
										Casual Helper
									</Text>
								</VStack>
							)}
						</HStack>
					</Flex>
					{isShowErrorPopup && (
						<Popup
							typeAlert="error"
							openAlert={isShowErrorPopup}
							message={messagePopup.message}
							details={messagePopup.detail}
							handleCloseAlert={(isShowPopup: boolean) => setIsShowErrorPopup(isShowPopup)}
						/>
					)}
					{isLoading && <LoadingOverlay openLoading={isLoading} />}
				</Form>
			)}
		</Formik>
	);
};

export default LoginPage;
