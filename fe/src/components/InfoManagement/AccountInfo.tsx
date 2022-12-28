import { Flex, VStack, HStack, Button } from "@chakra-ui/react";
import { Form, Formik, FormikErrors, FormikHelpers } from "formik";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FaRegUser } from "react-icons/fa";
import { HiOutlineMail } from "react-icons/hi";
import { RiLockPasswordLine, RiLockPasswordFill } from "react-icons/ri";
import { useSelector } from "react-redux";

import InputField from "../../common/shared-components/Input/InputField";
import { getCurrentWorker } from "../../app/reducer/loginAuth/loginAuthSlice";
import { useChangePasswordMutation } from "../../app/api/updateWorker/updateWorkerApi";
import { Popup } from "../../common/shared-components/Popup/Popup";
import { LoadingOverlay } from "../../common/shared-components/Loading/LoadingOverLay";

import { IAccountInfo } from "./types";

export const AccountInfo: React.FC = () => {
	const navigate = useNavigate();
	const [isDisableSave, setIsDisableSave] = useState<boolean>(true);
	const resultObj = useSelector(getCurrentWorker);
	const [changePassword, { isLoading: isChanging }] = useChangePasswordMutation();
	const [isShowSuccessPopup, setIsShowSuccessPopup] = useState<boolean>(false);
	const [isShowErrorPopup, setIsShowErrorPopup] = useState<boolean>(false);
	const [messagePopup, setMessagePopup] = useState({ message: "", detail: "" });

	const handleSubmit = async (values: IAccountInfo, actions: FormikHelpers<IAccountInfo>) => {
		changePassword({
			idUser: resultObj.id,
			oldPassword: values.oldPassword,
			newPassword: values.newPassword
		})
			.unwrap()
			.then(res => {
				if (res.isSuccessed) {
					setMessagePopup({
						...messagePopup,
						message: "Thành công",
						detail: "Bạn đã thay đổi mật khẩu thành công"
					});
					setIsShowSuccessPopup(true);
					actions.resetForm({
						values: { userName: values.userName, email: values.email, oldPassword: "", newPassword: "" }
					});
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
				setMessagePopup({
					...messagePopup,
					message: "Xảy ra lỗi",
					detail: error
				});
				setIsShowErrorPopup(true);
			});
	};
	const initialValues: IAccountInfo = {
		newPassword: "",
		oldPassword: "",
		userName: resultObj.userName,
		email: resultObj.email
	};
	return (
		<Formik
			initialValues={initialValues}
			validate={values => {
				const error: FormikErrors<IAccountInfo> = {};
				if (!values.oldPassword) {
					error.oldPassword = "Hãy điền mật khẩu cũ";
				}
				if (!values.newPassword) {
					error.newPassword = "Hãy điền mật khẩu mới";
				}
				if (
					values.oldPassword &&
					!/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,})/.test(values.oldPassword.trim())
				) {
					error.oldPassword =
						"Password must contain 8 Characters, one uppercase, one lowercase, one number and one special case character";
				}
				if (
					values.newPassword &&
					!/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,})/.test(values.newPassword.trim())
				) {
					error.newPassword =
						"Password must contain 8 Characters, one uppercase, one lowercase, one number and one special case character";
				}
				if (values.oldPassword && values.newPassword) {
					setIsDisableSave(false);
				} else {
					setIsDisableSave(true);
				}
				return error;
			}}
			onSubmit={handleSubmit}
			validateOnChange={true}
		>
			{() => (
				<Form>
					<Flex width={"100%"} height={"100%"} justifyContent={"center"} alignContent="center" marginTop={"50px"}>
						<VStack
							width={"100%"}
							height={"100%"}
							justifyContent={"center"}
							alignContent="center"
							spacing={7}
							padding={"0px 50px"}
						>
							<VStack spacing={6} width={"100%"} justifyContent="center" alignContent={"center"}>
								<InputField
									name="userName"
									label="Tên đăng nhập"
									inputProps={{
										fontSize: "20px",
										height: "50px",
										fontWeight: 400,
										width: "100%",
										color: "rgba(0, 0, 0)",
										isDisabled: true
									}}
									childrenIcon={<FaRegUser />}
								/>
								<InputField
									name="email"
									label="Email"
									inputProps={{
										fontSize: "20px",
										height: "50px",
										fontWeight: 400,
										width: "100%",
										color: "rgba(0, 0, 0)",
										isDisabled: true
									}}
									childrenIcon={<HiOutlineMail />}
								/>
							</VStack>
							<VStack spacing={7} width={"100%"} height="auto" paddingBottom="30px">
								<InputField
									name="oldPassword"
									label="Mật khẩu cũ"
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
								<InputField
									name="newPassword"
									label="Mật khẩu mới"
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
							</VStack>
							<HStack
								height={"10%"}
								width="100%"
								position={"relative"}
								bottom="0"
								alignContent={"center"}
								justifyContent={"center"}
								padding="0px 0px"
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
									Cancel
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
									isDisabled={isDisableSave}
								>
									Thay đổi mật khẩu
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
							handleCloseAlert={(isShowPopup: boolean) => setIsShowSuccessPopup(isShowPopup)}
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
					{isChanging && <LoadingOverlay openLoading={isChanging} />}
				</Form>
			)}
		</Formik>
	);
};
