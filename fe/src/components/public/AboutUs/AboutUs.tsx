import { Heading, HStack, VStack, Text, Image, Box } from "@chakra-ui/react";
import React from "react";

export const AboutUs: React.FC = () => {
	return (
		<VStack width={"100%"} height="100%" bgColor={"#fffcf8"} padding="10px" spacing={0}>
			<Heading
				width={"70%"}
				textAlign="center"
				paddingTop={"30px"}
				paddingBottom="20px"
				color="#532d29"
				fontWeight={"500"}
				fontSize="40px"
			>
				CHÚNG TÔI MANG ĐẾN NHỮNG GIÁ TRỊ TUYỆT VỜI NHẤT DỰA TRÊN{" "}
				<span style={{ color: "#d49457", fontStyle: "italic" }}>CHẤT LƯỢNG</span> VÀ TINH THẦN{" "}
				<span style={{ color: "#d49457", fontStyle: "italic" }}>NHIỆT HUYẾT</span>
			</Heading>
			<Text width={"15%"} textAlign="center" color={"#9c7d6e"} paddingBottom="10px">
				Bên cạnh sự thành công là những câu chuyện
			</Text>
			<Box
				width={"100%"}
				//backgroundColor={"red"}
				height="100%"
				margin={"10px 0px"}
			>
				<HStack
					width={"100%"}
					height="350px"
					justifyContent="center"
					padding={"0px 20px"}
					paddingTop="0px"
					position="relative"
					top={"0"}
					backgroundImage={"url('/images/aboutArrow2.png')"}
					backgroundSize="cover"
					backgroundRepeat={"no-repeat"}
					backgroundPosition="center"
				>
					<Image
						width={"230px"}
						height="250px"
						src="/images/aboutImg1.png"
						position={"absolute"}
						left="13%"
						opacity={"0.9"}
					/>
					<Image
						width={"100px"}
						height="150px"
						src="/images/aboutImg2.png"
						position={"absolute"}
						left="28%"
						opacity={"0.8"}
					/>
					<Image
						width={"50px"}
						height="60px"
						src="/images/aboutImg3.png"
						position={"absolute"}
						left="35%"
						opacity={"1"}
					/>
					<Image
						width={"200px"}
						height="200px"
						src="/images/aboutImg4.png"
						position={"absolute"}
						left="38%"
						opacity={"0.7"}
					/>
					<Image
						width={"200px"}
						height="150px"
						src="/images/aboutImg5.png"
						position={"absolute"}
						left="53%"
						opacity={"1"}
					/>
					<Image
						width={"200px"}
						height="230px"
						src="/images/aboutImg6.png"
						position={"absolute"}
						left="67%"
						opacity={"0.8"}
					/>
					<Image
						width={"70px"}
						height="70px"
						src="/images/aboutImg7.png"
						position={"absolute"}
						left="82%"
						opacity={"0.8"}
					/>
				</HStack>
			</Box>

			<VStack width={"100%"} paddingTop="100px" justifyContent={"flex-start"} bgColor={"#fffcf8"}>
				<Heading fontWeight={"500"} textAlign="left" width={"90%"}>
					Câu chuyện của chúng tôi
				</Heading>
				<Text></Text>
			</VStack>
		</VStack>
	);
};
