import { Flex, VStack, Text, HStack, Img } from "@chakra-ui/react";
import React, { useState } from "react";

import { IUserContactRegister, IUserLoginRegister, IUserPersonalRegister } from "../app/api/register/registerType";
import { userContactInfoEmpty, userLoginInfoEmpty, userPersonalInfoEmpty } from "../common/constants/constants";
import LoginInfo from "../components/register/LoginInfo";
import WorkerContactInfo from "../components/register/WorkerContactInfo";
import WorkerPersonalInfo from "../components/register/WorkerPersonalInfo";

const RegisterPage: React.FC = () => {
	const [page, setPage] = useState(0);
	const [userLoginInfo, setUserLoginInfo] = useState<IUserLoginRegister>(userLoginInfoEmpty);
	const [userPersonalInfo, setUserPersonalInfo] = useState<IUserPersonalRegister>(userPersonalInfoEmpty);
	const [userContactInfo, setUserContactInfo] = useState<IUserContactRegister>(userContactInfoEmpty);

	const handleTurnPage = (page: number) => {
		setPage(page);
	};

	const handleGetLoginInfo = (loginInfo: IUserLoginRegister) => {
		setUserLoginInfo(loginInfo);
	};
	const handleGetPersonalInfo = (personalInfo: IUserPersonalRegister) => {
		setUserPersonalInfo(personalInfo);
	};
	const handleGetContactInfo = (contactInfo: IUserContactRegister) => {
		setUserContactInfo(contactInfo);
	};
	return (
		<Flex
			flexDirection="column"
			width="100vw"
			height="100vh"
			justifyContent="center"
			alignItems="center"
			position="relative"
			backgroundColor={"#faf2e5"}
		>
			<HStack
				width={"80vw"}
				height={page === 2 || page === 3 ? "95vh" : "75vh"}
				bgColor="#f9d475"
				spacing={0}
				boxShadow="8px 8px 10px rgb(249, 212, 117, 0.6)"
			>
				{/* Logo */}
				<VStack width="50%" height="100%" bgColor="#f9d475" justify="center" align="center" position="relative">
					<HStack justifyContent={"center"} align={"center"}>
						<Img w={{ md: "230px", lg: "300px" }} h={{ md: "230px", lg: "300px" }} src="/images/Login-removeBg.png" />
					</HStack>
					<Text fontSize="18px" fontWeight="500" bgClip="text" color={"#000000"} position="absolute" bottom="16px">
						Casual Helper
					</Text>
				</VStack>
				{/* Register */}
				<VStack
					position="relative"
					spacing={4}
					width="100%"
					height="100%"
					bgColor={"white"}
					justifyContent={"flex-start"}
					boxSizing="border-box"
					padding={"0px 20px"}
				>
					{page === 2 ? (
						<WorkerPersonalInfo
							handleNextPage={handleTurnPage}
							handleBackPage={handleTurnPage}
							handleGetPersonalInfo={handleGetPersonalInfo}
							userPersonalInfo={userPersonalInfo}
						/>
					) : page === 3 ? (
						<WorkerContactInfo
							handleBackPage={handleTurnPage}
							userLoginInfo={userLoginInfo}
							userPersonalInfo={userPersonalInfo}
							handleGetContactInfo={handleGetContactInfo}
							userContactInfo={userContactInfo}
						/>
					) : (
						<LoginInfo
							handleNextPage={handleTurnPage}
							handleGetLoginInfo={handleGetLoginInfo}
							userLoginInfo={userLoginInfo}
						/>
					)}
				</VStack>
			</HStack>
		</Flex>
	);
};

export default RegisterPage;
