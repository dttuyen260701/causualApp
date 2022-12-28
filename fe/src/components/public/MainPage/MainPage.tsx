import React from "react";
import { Box, Heading, HStack, Image, VStack, Text, Img, AspectRatio, Flex } from "@chakra-ui/react";
import { AiTwotonePhone } from "react-icons/ai";
import { ImLocation } from "react-icons/im";
import { MdEmail } from "react-icons/md";
import { BsFacebook } from "react-icons/bs";
import { AiFillInstagram, AiFillYoutube, AiFillTwitterCircle } from "react-icons/ai";

import { QualityCardGroup } from "../QualityCardGroup/QualityCardGroup";
import { FeatureCardGroup } from "../FeatureCardGroup/FeatureCardGroup";
import { TypeOfJob } from "../TypeOfJob/TypeOfJob";

export const MainPage: React.FC = () => {
	return (
		<VStack width={"100%"} padding="0px" spacing={5} backgroundColor="#fff3e2">
			<HStack width={"100%"} justifyContent="space-between" position={"relative"} paddingBottom="100px">
				<VStack width={"50%"}>
					<Heading color={"#000000"}>ỨNG DỤNG TÌM KIẾM</Heading>
					<Heading color={"#f9d475"}>DỊCH VỤ THỜI VỤ</Heading>
					<Text width={"70%"} align="left" paddingTop={"40px"} color="#9c7d6e">
						Tìm kiếm những dịch vụ tốt nhất hỗ trợ cho các công việc hàng ngày trong gia đình bạn như là vệ sinh nhà
						cửa, sửa chữa thiết bị ...
					</Text>
					<HStack width={"70%"} justifyContent="flex-start" paddingTop={"20px"}>
						<Box
							color="#000000"
							bgColor={"#f9d475"}
							borderRadius="40px"
							padding={"10px"}
							boxShadow="8px 8px 10px rgb(249, 212, 117, 0.6)"
							cursor="pointer"
						>
							<Text>Tải app ngay</Text>
						</Box>
					</HStack>
					<HStack width={"100%"} spacing={0} padding="0px 10px" paddingTop="50px">
						<VStack width={"35%"} justify="center">
							<Text fontSize={"40px"} fontWeight="600" color={"#f9d475"}>
								20<span style={{ color: "#000000" }}>+</span>
							</Text>
							<Text align={"center"} color="#9c7d6e">
								Dịch vụ thời vụ
							</Text>
						</VStack>
						<VStack width={"30%"}>
							<Text fontSize={"40px"} fontWeight="600" color={"#f9d475"}>
								10K<span style={{ color: "#000000" }}>+</span>
							</Text>
							<Text align={"center"} color="#9c7d6e">
								Khách hành thân thiết
							</Text>
						</VStack>
						<VStack width={"35%"}>
							<Text fontSize={"40px"} fontWeight="600" color={"#f9d475"}>
								10K<span style={{ color: "#000000" }}>+</span>
							</Text>
							<Text align={"center"} color="#9c7d6e">
								Thợ có tay nghề cao
							</Text>
						</VStack>
					</HStack>
				</VStack>
				<Box
					backgroundColor={"#f6d79f"}
					width="50%"
					height={"100%"}
					borderTopLeftRadius="350px"
					backgroundImage={"url(./images/publicBlur.png)"}
					backgroundSize="cover"
					backgroundRepeat={"none"}
					backgroundPosition="center"
				>
					<Image
						src="./images/publicLogo.png"
						width={"100px"}
						height="100px"
						position={"relative"}
						top="38"
						left={"50"}
						borderTopEndRadius="80%"
						borderTopStartRadius={"80%"}
						borderBottomEndRadius="80%"
						borderBottomStartRadius={"80%"}
						boxShadow="0px 0px 7px 0px black"
					/>
					<Image
						src="./images/public.png"
						position={"relative"}
						top="35"
						left={"40"}
						width={"400px"}
						height="400px"
						borderTopEndRadius="80%"
						borderTopStartRadius={"80%"}
						border="7px solid #faf2e5"
						boxShadow="7px 7px 7px #f6d79f"
						transform="scale(.9)"
					/>
				</Box>
			</HStack>

			{/* Who we are */}
			<HStack
				width={"80vw"}
				height="70vh"
				bgColor={"#f9d475"}
				boxShadow="8px 8px 10px rgb(249, 212, 117, 0.6)"
				borderRadius={"10px"}
			>
				<VStack
					width="100%"
					height="100%"
					bgColor="#f9d475"
					justify="center"
					align="center"
					position="relative"
					borderTopStartRadius={"10px"}
					borderBottomStartRadius="10px"
				>
					<Img
						width={"100%"}
						height="100%"
						src="/images/bgUs.jpg"
						borderTopStartRadius={"10px"}
						borderBottomStartRadius="10px"
					/>
				</VStack>
				<VStack width="100%" height="100%" alignItems={"center"} justifyContent="center" padding={"0px 10px"}>
					<Heading color={"#000000"} fontWeight="300" fontSize={"40px"} paddingBottom="10px">
						CHÚNG TÔI LÀ AI?
					</Heading>
					<Text align={"center"} color="#000000">
						Chúng tôi là một startup với ứng dụng công nghệ đầu tiên vào ngành giúp việc thời vụ ở Việt Nam. Chúng tôi
						cung cấp đa dịch vụ tiện ích như: dọn dẹp nhà, vệ sinh máy lạnh, đi chợ, … tại Việt Nam. Thông qua ứng dụng
						đặt lịch dành cho khách hàng và ứng dụng nhận việc của các thợ, khách hàng và các thợ có thể chủ động đăng
						và nhận việc trực tiếp trên ứng dụng.
					</Text>
				</VStack>
			</HStack>

			{/* Ways we have */}
			<VStack>
				{/* Heading */}
				<VStack paddingTop={"60px"} paddingBottom="20px">
					<Heading color={"#000000"}>Những dịch vụ chúng tôi có</Heading>
					<Text color={"#9c7d6e"}>Tất cả những tiện ích gia đình mà bạn cần</Text>
				</VStack>
				<TypeOfJob />
			</VStack>

			{/* Quality */}
			<HStack paddingTop={"150px"} paddingBottom="100px">
				<VStack width={"50%"}>
					<Image
						src="./images/public2.png"
						border="8px solid #faf2e5"
						boxShadow="7px 7px 7px #f6d79f"
						borderTopEndRadius="80%"
						borderTopStartRadius={"80%"}
						width="80%"
					></Image>
				</VStack>
				<VStack width={"50%"}>
					<Heading fontWeight="700" color={"#000000"}>
						An tâm với <span style={{ color: "#f9d475" }}>sự lựa chọn</span> của bạn
					</Heading>
					<Text width={"80%"} align="left" paddingTop={"20px"} color="#9c7d6e">
						Những dịch vụ của chúng tôi mang chất lượng tốt nhất, phù hợp với điều kiện giải quyết các vấn đề trong gia
						đình bạn. Đó là lý do bạn chọn chúng tôi.
					</Text>
					<QualityCardGroup />
				</VStack>
			</HStack>

			<HStack padding={"150px 0px"}>
				<VStack width={"50%"} spacing={10}>
					<Heading fontWeight="700" color={"#000000"}>
						Những <span style={{ color: "#f9d475" }}>tính năng mới</span> của ứng dụng
					</Heading>
					<Text width={"80%"} align="center" paddingTop={"20px"} color="#9c7d6e">
						Chúng tôi luôn cập nhật những tính năng mới nhất để đáp ứng những nhu cầu hàng ngày của mỗi khách hàng. Bạn
						có thể trải nghiệm những tính năng ấy ở mỗi dịch vụ bạn sử dụng
					</Text>
					<FeatureCardGroup />
				</VStack>
				<VStack width={"50%"}>
					<Image src="./images/public3.png" boxShadow="7px 7px 7px #f6d79f" width="80%" height={"80vh"}></Image>
				</VStack>
			</HStack>

			<VStack
				width={"80%"}
				spacing={10}
				backgroundColor="#fdf8ea"
				justifyContent={"center"}
				align="center"
				padding={"50px"}
				borderRadius="20px"
			>
				<Heading color={"#000000"}>
					Hãy <span style={{ color: "#f9d475" }}>trải nghiệm ngay</span> cùng chúng tôi
				</Heading>
				<Text width={"60%"} align="center" color="#9c7d6e">
					Hãy trải nghiệm sản phẩm của chúng tôi và tận hưởng những giá trị tuyệt vời của chúng
				</Text>
				<Box
					color="#000000"
					bgColor={"#f9d475"}
					borderRadius="40px"
					padding={"10px"}
					boxShadow="8px 8px 10px rgb(249, 212, 117, 0.6)"
					cursor="pointer"
				>
					<Text>Tải app ngay</Text>
				</Box>
			</VStack>

			<Box minWidth={"100%"} minHeight="20%" padding="100px 0px">
				<AspectRatio ratio={21 / 9}>
					<iframe
						src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3833.8374416354745!2d108.14770341462636!3d16.073923188878013!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x314218d68dff9545%3A0x714561e9f3a7292c!2zVHLGsOG7nW5nIMSQ4bqhaSBo4buNYyBCw6FjaCBLaG9hIC0gxJDhuqFpIGjhu41jIMSQw6AgTuG6tW5n!5e0!3m2!1svi!2s!4v1668759641745!5m2!1svi!2s"
						width="600"
						height="450"
						style={{ border: 0 }}
						allowFullScreen={true}
						loading="lazy"
						referrerPolicy="no-referrer-when-downgrade"
					></iframe>
				</AspectRatio>
			</Box>
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
								<Text color={"#9c7d6e"}>info@casualhelper.vn</Text>
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

				<Box backgroundColor={"#fcf8e9"} width="100%">
					<Text fontSize="14px" color="#9c7d6e" textAlign="center">
						© 2022 CasualHelper. Proudly created by casual team
					</Text>
				</Box>
			</VStack>
		</VStack>
	);
};
