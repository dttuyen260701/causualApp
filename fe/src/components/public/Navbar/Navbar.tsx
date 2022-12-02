import { Box, Heading, HStack, VStack, Text } from "@chakra-ui/react";
import React from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";

import { setNameItemNav } from "../../../app/reducer/nameItemNav/nameItemNavSlice";

import { ItemNavbar } from "./ItemNavbar";

export const Navbar: React.FC = () => {
	const navigate = useNavigate();
	const dispatch = useDispatch();

	return (
		<VStack width={"100%"} padding="0px" spacing={5} backgroundColor="#fffcf8">
			{/* Header */}
			<Box
				width={"100%"}
				height="auto"
				backgroundColor="#fffcf8"
				padding={"30px 0px"}
				position="sticky"
				top={"0"}
				zIndex="2"
				opacity="1"
			>
				<HStack
					width={"100%"}
					height={"auto"}
					justifyContent={"space-around"}
					alignContent="center"
					position={"fixed"}
					top={"0"}
					left="0"
					right={"0"}
					margin={"10px 0px"}
					paddingRight="20px"
				>
					<Heading fontWeight={"500"} color="#532d29">
						Casual Web
					</Heading>
					<Box>
						<HStack spacing={10}>
							<ItemNavbar
								name="Trang chủ"
								navigate="/public"
								handleClickItem={() => dispatch(setNameItemNav("Trang chủ"))}
							/>
							<ItemNavbar
								name="Về chúng tôi"
								navigate="/public/about-us"
								handleClickItem={() => dispatch(setNameItemNav("Về chúng tôi"))}
							/>
							<ItemNavbar
								name="Liên lạc"
								navigate="/public/contact"
								handleClickItem={() => dispatch(setNameItemNav("Liên lạc"))}
							/>
							<ItemNavbar
								name="Our team"
								navigate="/public/team"
								handleClickItem={() => dispatch(setNameItemNav("Our team"))}
							/>
						</HStack>
					</Box>
					<Box
						_hover={{ color: "#f6d79f" }}
						_active={{ color: "#f6d79f" }}
						color="white"
						bgGradient="linear-gradient(to right, #d49457 10%,#c75422 90%)"
						borderRadius="40px"
						padding={"10px"}
						boxShadow="7px 7px 10px #FDB493"
						cursor={"pointer"}
						onClick={() => navigate("/register")}
					>
						<HStack justifyContent={"center"}>
							<Text fontWeight={"400"}>Trở thành đối tác</Text>
						</HStack>
					</Box>
				</HStack>
			</Box>
		</VStack>
	);
};
