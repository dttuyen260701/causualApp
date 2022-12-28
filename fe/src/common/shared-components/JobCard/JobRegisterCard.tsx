import { HStack, Image, VStack, Text } from "@chakra-ui/react";
import React from "react";
import { BsArrowUpRight } from "react-icons/bs";
import { GiMoneyStack } from "react-icons/gi";

import { API_URL } from "../../utils/config";

import { IJobInfoCard } from "./type";

export const JobRegisterCard: React.FC<IJobInfoCard> = ({ jobInfo, handleClickRegister }) => {
	return (
		<HStack
			width={"90%"}
			alignItems="flex-start"
			justifyContent={"space-between"}
			borderBottom="1px solid #d0c5ba"
			padding={"10px"}
		>
			<HStack>
				<Image src={`${API_URL}${jobInfo.image}`} width={"100px"} />
				<VStack width={"100%"} alignItems={"flex-start"} spacing={0}>
					<Text textAlign={"left"} width={"100%"} fontWeight="700" fontSize={"20px"} color="#000000">
						{jobInfo.name}
					</Text>
					<Text textAlign={"left"} width={"100%"} paddingBottom="15px" color={"#532d29"}>
						{jobInfo.description}
					</Text>
					<HStack spacing={2} border="1px solid #d0c5ba" padding={"3px"} borderRadius="15px">
						<GiMoneyStack color="#9c7d6e" />
						<Text color={"#9c7d6e"}>{`${jobInfo.price} VND/1 dịch vụ`}</Text>
					</HStack>
				</VStack>
			</HStack>
			<HStack cursor={"pointer"} onClick={() => handleClickRegister(jobInfo.id)}>
				<Text fontWeight={700} color="#000000">
					Đăng ký ngay
				</Text>
				<BsArrowUpRight color="#000000" size={30} />
			</HStack>
		</HStack>
	);
};
