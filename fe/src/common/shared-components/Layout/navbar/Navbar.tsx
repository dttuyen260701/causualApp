import { Heading, HStack, useMediaQuery, Image, Box } from "@chakra-ui/react";
import React from "react";
import { useLocation } from "react-router-dom";
import { useSelector } from "react-redux";

import { getCurrentWorker } from "../../../../app/reducer/loginAuth/loginAuthSlice";
import { API_URL } from "../../../utils/config";

import { IProfileNavbar } from "./type";
import ProfileNavbar from "./ProfileNavbar";

const Navbar: React.FC = () => {
	const [showSideBar] = useMediaQuery("(min-width: 1100px)");
	const resultObj = useSelector(getCurrentWorker);
	const location = useLocation();

	const locationPath: string[] = location?.pathname.split("/");
	if (locationPath.length > 0) {
		locationPath.shift();
	}
	const profileNavbar: IProfileNavbar = {
		image: `${API_URL}${resultObj.avatar}`,
		name: resultObj.name,
		email: resultObj.email
	};

	return (
		<HStack
			w="100%"
			h={showSideBar ? "65px" : "70px"}
			bgColor="#f9d475"
			minHeight="60px"
			spacing="0px"
			align="center"
			paddingY={showSideBar ? "5px" : "0px"}
			paddingRight={{ sm: "10px", md: "24px" }}
			paddingLeft={{ sm: "10px", md: "24px" }}
			shadow="xl"
			borderBottomWidth={showSideBar ? "" : "2px"}
			borderBottomColor={showSideBar ? "" : "primary"}
		>
			<HStack
				width={{ sm: "70%", md: "50%" }}
				height="100%"
				alignItems="center"
				justifyContent="flex-start"
				spacing={0}
			>
				{!showSideBar && (
					<HStack spacing={2} alignContent="center" width={"70%"}>
						<Box>
							<Image src="/images/logoWeb-removeBg.png" width={"70px"} height={"60px"} />
						</Box>
						<Heading fontSize="20px" fontWeight={"500"} width="100%">
							Quản lý thông tin
						</Heading>
					</HStack>
				)}
			</HStack>

			<ProfileNavbar {...profileNavbar} />
		</HStack>
	);
};

export default Navbar;
