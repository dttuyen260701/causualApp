import { HStack, Image, VStack, Text } from "@chakra-ui/react";
import React from "react";
import { AiOutlineClose } from "react-icons/ai";
import { BiCalendarAlt } from "react-icons/bi";
import { GiMoneyStack } from "react-icons/gi";
import { useSelector } from "react-redux";

import { getCurrentWorker } from "../../../app/reducer/loginAuth/loginAuthSlice";
import { API_URL } from "../../utils/config";

import { IJobInfoCard } from "./type";

export const JobInfoCard: React.FC<IJobInfoCard> = ({ jobInfo, handleDeleteJobInfo }) => {
	const resultObj = useSelector(getCurrentWorker);
	return (
		<HStack
			boxShadow="5px 5px 3px rgb(249, 212, 117, 0.4)"
			width={"90%"}
			borderRadius="20px"
			justifyContent={"space-between"}
		>
			<HStack>
				<Image src={`${API_URL}${jobInfo.image}`} width={"120px"} borderStartRadius="20px" />
				<VStack width={"100%"} alignItems={"flex-start"} spacing={0}>
					<Text textAlign={"left"} width={"100%"} fontWeight="700" fontSize={"20px"} color="#000000">
						{jobInfo.name}
					</Text>
					<Text textAlign={"left"} width={"100%"} paddingBottom="15px" color={"#532d29"}>
						{jobInfo.note !== "" ? jobInfo.note : jobInfo.description}
					</Text>
					<HStack spacing={2} border="1px solid #d0c5ba" padding={"3px"} borderRadius="15px">
						<GiMoneyStack color="#9c7d6e" />
						<Text color={"#9c7d6e"}>{`${jobInfo.price} VND/1 dịch vụ`}</Text>
					</HStack>
				</VStack>
			</HStack>
			<VStack spacing={6} alignItems="flex-end" paddingRight={"10px"}>
				<AiOutlineClose
					color="#000000"
					size={20}
					cursor="pointer"
					onClick={() => handleDeleteJobInfo({ jobInfoId: jobInfo.id, workerId: resultObj.id })}
				/>
				<HStack spacing={2} border="1px solid #000000" padding={"3px"} borderRadius="15px">
					<BiCalendarAlt color="#000000" cursor={"pointer"} />
					<Text color={"#000000"}>{jobInfo.creationTime}</Text>
				</HStack>
			</VStack>
		</HStack>
	);
};
