import { Heading, VStack, Text, HStack, Image } from "@chakra-ui/react";
import React from "react";
import { AiTwotonePhone } from "react-icons/ai";
import { ImLocation } from "react-icons/im";
import { MdEmail } from "react-icons/md";

export const Contact: React.FC = () => {
	return (
		<VStack
			width={"100%"}
			height="100%"
			backgroundColor={"#fff3e2"}
			justifyContent="space-between"
			alignContent={"center"}
		>
			<VStack padding="40px 0px">
				<Heading color={"#000000"}>Liên lạc với chúng tôi để có nhiều thông tin bổ ích</Heading>
				<Text color={"#f9d475"}>
					Nếu bạn cần sự giúp đỡ hay bất kì câu hỏi nào, chúng tôi luôn sẵn sàng giải đáp thắc mắc
				</Text>
				<HStack spacing={20} paddingTop="50px">
					<VStack bgColor={"#fdf8ea"} width="50%" padding="20px" borderRadius={"20px"}>
						<ImLocation />
						<Text color={"#9c7d6e"} align="center">
							54 Nguyễn Lương Bằng, Đà Nẵng
						</Text>
					</VStack>
					<VStack bgColor={"#fdf8ea"} width="40%" padding="20px" borderRadius={"20px"}>
						<AiTwotonePhone />
						<Text color={"#9c7d6e"}>090934998527</Text>
					</VStack>
					<VStack bgColor={"#fdf8ea"} width="40%" padding="20px" borderRadius={"20px"}>
						<MdEmail />
						<Text color={"#9c7d6e"}>info@casualhelper.vn</Text>
					</VStack>
				</HStack>
			</VStack>
			<Image width={"50%"} height="47%" src="/images/contactPage-removebg.png" />
		</VStack>
	);
};
