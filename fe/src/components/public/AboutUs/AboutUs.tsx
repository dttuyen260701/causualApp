import { Heading, HStack, VStack, Text, Image, Box, Flex } from "@chakra-ui/react";
import React from "react";
import { AiFillInstagram, AiFillTwitterCircle, AiFillYoutube, AiTwotonePhone } from "react-icons/ai";
import { BsFacebook } from "react-icons/bs";
import { ImLocation } from "react-icons/im";
import { MdEmail } from "react-icons/md";

export const AboutUs: React.FC = () => {
	return (
		<VStack width={"100%"} height="100%" bgColor={"#fff3e2"} padding="0px" spacing={0}>
			<Heading
				width={"70%"}
				textAlign="center"
				paddingTop={"30px"}
				paddingBottom="20px"
				color="#000000"
				fontWeight={"500"}
				fontSize="40px"
			>
				CHÚNG TÔI MANG ĐẾN NHỮNG GIÁ TRỊ TUYỆT VỜI NHẤT DỰA TRÊN{" "}
				<span style={{ color: "#f9d475", fontStyle: "italic" }}>CHẤT LƯỢNG</span> VÀ TINH THẦN{" "}
				<span style={{ color: "#f9d475", fontStyle: "italic" }}>NHIỆT HUYẾT</span>
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

			<VStack width={"100%"} paddingTop="100px" justifyContent={"flex-start"} bgColor={"#fff3e2"} spacing={10}>
				<Heading color={"#f9d475"} fontWeight={"500"} textAlign="left" width={"100%"} paddingLeft="30px">
					Câu chuyện của chúng tôi
				</Heading>
				<VStack width={"100%"} alignItems="flex-start" spacing={5} padding="0px 50px">
					<Text width={"80%"} textAlign={"justify"} color={"#000000"}>
						Hiện nay ở Việt Nam, có rất nhiều hộ gia đình, người sinh sống một mình hay các tập thể đều có nhu cầu rất
						cao về việc tìm kiếm người giúp việc thời vụ trong nhiều lĩnh vực như dọn dẹp, vệ sinh nhà cửa, bưng vác vật
						nặng, sửa chữa các thiết bị điện và nước,... Tuy nhiên không phải ai cũng dễ dàng tìm kiếm những người thợ
						có tay nghề cao, uy tín với một giá thành hợp lý. Đa số mọi người đều gặp khó khăn khi tìm những người thợ
						giỏi về những lĩnh vực khác nhau.
					</Text>
					<Text width={"80%"} textAlign="left" color={"#000000"}>
						Nắm bắt tình hình đó, Casual Helper - Hệ thống quản lý và tìm kiếm người giúp việc thời vụ đã được ra đời.
					</Text>
				</VStack>
				<VStack width={"100%"} height="70%" paddingTop={"100px"}>
					<Image src="/images/picture1.png" height={"70%"} width="100%" />
				</VStack>
				<VStack
					width={"100%"}
					spacing={20}
					justifyContent="center"
					alignContent={"center"}
					padding={"0px 50px"}
					paddingBottom={"200px"}
				>
					<HStack width={"100%"} spacing={20}>
						<VStack width={"100%"} alignItems="flex-start">
							<Text width={"100%"} textAlign="left" fontSize={"40px"} color="#f9d475" fontWeight={"500"}>
								Tầm nhìn
							</Text>
							<Text width={"70%"} textAlign="left" color={"#000000"} fontSize="17px">
								Casual Helper luôn cố nỗ lực để tiếp cận đến nhiều khách hàng, trở thành ứng dụng có lượt sử dụng cao
								nhất
							</Text>
						</VStack>
						<VStack width={"100%"} alignItems="flex-start">
							<Text width={"100%"} textAlign="left" fontSize={"40px"} color="#f9d475" fontWeight={"500"}>
								Sứ mệnh
							</Text>
							<Text width={"70%"} textAlign="left" color={"#000000"} fontSize="17px">
								Casual Helper luôn đáp ứng các nhu cầu từ khách hàng một cách chuyên nghiệp và nhanh chóng
							</Text>
						</VStack>
					</HStack>
					<HStack width={"100%"} spacing={20}>
						<VStack width={"100%"} alignItems="flex-start">
							<Text width={"100%"} textAlign="left" fontSize={"40px"} color="#f9d475" fontWeight={"500"}>
								Tận tâm
							</Text>
							<Text width={"70%"} textAlign="left" color={"#000000"} fontSize="17px">
								Luôn đặt mình về phía khách hàng để thấu hiểu và mang lại giá trị tốt nhất
							</Text>
						</VStack>
						<VStack width={"100%"} alignItems="flex-start">
							<Text width={"100%"} textAlign="left" fontSize={"40px"} color="#f9d475" fontWeight={"500"}>
								Cải tiến
							</Text>
							<Text width={"70%"} textAlign="left" color={"#000000"} fontSize="17px">
								Chúng tôi luôn lắng nghe ý kiến từ khách hàng để không ngừng đổi mới, đáp ứng nhiều nhu cầu từ phía
								người dùng
							</Text>
						</VStack>
					</HStack>
				</VStack>

				<VStack width={"100%"} backgroundColor={"#fcf8e9"}>
					<HStack width={"100%"} paddingBottom="80px" paddingTop={"60px"} justifyContent="space-between">
						<VStack width={"50%"} alignContent="center" justifyContent={"center"}>
							<Image src="/images/logoWeb-removeBg.png" width={"110px"} height={"110px"} />
							<Heading fontWeight={"500"} width={"50%"} color="#f9d475" fontSize={"40px"}>
								Casual Helper
							</Heading>
						</VStack>

						<Flex flexDir={"row"} width={"70%"} alignContent="flex-start">
							<VStack alignContent="flex-start" paddingRight={"50px"}>
								<HStack alignContent={"flex-start"} width="100%">
									<Text fontWeight={"500"} color="#f9d475" fontSize={"25px"}>
										Liên hệ
									</Text>
								</HStack>
								<HStack alignContent={"flex-start"} width="100%">
									<AiTwotonePhone />
									<Text color={"#9c7d6e"}>090934998527</Text>
								</HStack>
								<HStack alignContent={"flex-start"} width="100%">
									<MdEmail />
									<Text color={"#9c7d6e"}>info@casualweb.vn</Text>
								</HStack>
							</VStack>

							<VStack alignContent="flex-start" width={"30%"}>
								<HStack alignContent={"flex-start"} width="100%" paddingLeft={"25px"}>
									<Text fontWeight={"500"} color="#f9d475" fontSize={"25px"}>
										Địa chỉ
									</Text>
								</HStack>

								<Flex flexDir={"row"} width="100%">
									<ImLocation />
									<Text width={"80%"} paddingLeft="10px" color={"#9c7d6e"}>
										54 Nguyễn Lương Bằng, Quận Liên Chiểu, Đà Nẵng
									</Text>
								</Flex>
							</VStack>

							<VStack justifyContent={"center"} height="100%">
								<HStack>
									<Text fontWeight={"500"} color="#f9d475" fontSize={"25px"}>
										Trang thông tin
									</Text>
								</HStack>
								<HStack>
									<BsFacebook size={"20"} cursor="pointer" color="#f9d475" />
									<AiFillInstagram size={"25"} cursor="pointer" color="#532d29" />
									<AiFillYoutube size={"25"} cursor="pointer" color="#f9d475" />
									<AiFillTwitterCircle size={"25"} cursor="pointer" color="#532d29" />
								</HStack>
							</VStack>
						</Flex>
					</HStack>

					<Box backgroundColor={"#fff3e2"} width="100%">
						<Text fontSize="14px" color="#9c7d6e" textAlign="center">
							© 2022 CasualWeb. Proudly created by casual team
						</Text>
					</Box>
				</VStack>
			</VStack>
		</VStack>
	);
};
