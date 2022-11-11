import { Flex, VStack, Text, HStack, Img } from "@chakra-ui/react";
import React, { useState } from "react";

import LoginInfo from "../components/register/LoginInfo";
import WorkerContactInfo from "../components/register/WorkerContactInfo";
import WorkerPersonalInfo from "../components/register/WorkerPersonalInfo";

const RegisterPage: React.FC = () => {
	const [page, setPage] = useState(0);

	const handleTurnPage = (page: number) => {
		setPage(page);
	};
	return (
		<Flex
			flexDirection="column"
			width="100vw"
			height="100vh"
			justifyContent="center"
			alignItems="center"
			position="relative"
		>
			<HStack
				width={"80vw"}
				height={page === 2 || page === 3 ? "95vh" : "75vh"}
				bgColor="#E48D41"
				spacing={0}
				boxShadow="7px 7px 10px #FDB493"
				overflow={"scroll"}
			>
				{/* Logo */}
				<VStack width="50%" height="100%" bgColor="#E48D41" justify="center" align="center" position="relative">
					<HStack justifyContent={"center"} align={"center"}>
						<Img w={{ md: "230px", lg: "300px" }} h={{ md: "230px", lg: "300px" }} src="/images/Login.png" />
					</HStack>
					<Text
						fontSize="18px"
						fontWeight="bold"
						bgClip="text"
						bgGradient="linear-gradient(to right, #f2ed5e 10%,#edc764 90%);"
						position="absolute"
						bottom="16px"
					>
						Casual Manager
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
				>
					{page === 2 ? (
						<WorkerPersonalInfo handleNextPage={handleTurnPage} handleBackPage={handleTurnPage} />
					) : page === 3 ? (
						<WorkerContactInfo handleBackPage={handleTurnPage} />
					) : (
						<LoginInfo handleNextPage={handleTurnPage} />
					)}
				</VStack>
			</HStack>
		</Flex>
	);
};

export default RegisterPage;
