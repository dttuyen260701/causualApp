import { Heading, HStack, VStack, Text, Button } from "@chakra-ui/react";
import React, { useState } from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import SwiperCore, { Navigation, Pagination, EffectCoverflow, Autoplay } from "swiper";
// Import Swiper styles
import "swiper/swiper.min.css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import "swiper/css/effect-coverflow";
import "swiper/css/autoplay";
import { BsArrowLeft, BsArrowRight } from "react-icons/bs";

import { TeamMemberCard } from "./TeamMemberCard";
import { IMemberCard } from "./types";

SwiperCore.use([Navigation, Pagination, EffectCoverflow, Autoplay]);

export const OurTeam: React.FC = () => {
	const [prevEl, setPrevEl] = useState<HTMLElement | null>(null);
	const [nextEl, setNextEl] = useState<HTMLElement | null>(null);
	const teamMembers: IMemberCard[] = [
		{ nameMember: "Nguyễn Khánh Huy", position: "BE Developer", image: "/images/huy.jpg" },
		{ nameMember: "Lê Minh Lợi", position: "BE Developer", image: "/images/lml.jpg" },
		{ nameMember: "Đoàn Thị Diệu Hằng", position: "FE Developer", image: "/images/hang.jpg" },
		{ nameMember: "Lưu Yến Nhi", position: "Mobile Developer", image: "/images/nhinhi.jpg" },
		{ nameMember: "Đặng Thanh Tuyên", position: "Mobile Developer", image: "/images/tuyen.png" }
	];
	const [activeTeamMember, setActiveTeamMember] = useState<IMemberCard>({ nameMember: "", position: "", image: "" });

	return (
		<VStack width={"100%"} height={"100%"} spacing={7} padding={"20px 0px"} backgroundColor="#fff3e2">
			<VStack justifyContent={"flex-start"}>
				<Heading fontSize={"50"} fontWeight="500" color={"#332c47"}>
					Gặp gỡ đội ngũ của chúng ta
				</Heading>
			</VStack>
			<VStack spacing={0} width="100%">
				<Swiper
					navigation={{
						prevEl,
						nextEl
					}}
					pagination={{ clickable: true }}
					effect="coverflow"
					coverflowEffect={{
						rotate: 5,
						stretch: 0,
						depth: 100,
						modifier: 2.5,
						slideShadows: true
					}}
					autoplay={{ delay: 2000, disableOnInteraction: true, reverseDirection: false }}
					slidesPerView={3}
					centeredSlides={false}
					loop={true}
					grabCursor={true}
					onActiveIndexChange={index => {
						const item = index.activeIndex < 7 ? index.activeIndex - 2 : index.activeIndex === 7 ? 0 : 1;
						setActiveTeamMember(teamMembers[item]);
					}}
					style={{ height: "300px", width: "75vw" }}
				>
					{teamMembers.map((member, index) => {
						return (
							<SwiperSlide key={index}>
								<TeamMemberCard key={index} image={member.image} />
							</SwiperSlide>
						);
					})}
				</Swiper>
				<VStack>
					<VStack width={"100%"} height="100%" padding={"25px 10px"}>
						<Heading fontSize={"30px"} fontWeight="400" color={"#000000"}>
							{activeTeamMember.nameMember}
						</Heading>
						<Text
							fontSize={"16px"}
							align="center"
							padding={"10px"}
							color="#9c7d6e"
							// border={"1px solid #9c7d6e"}
							borderRadius="30px"
						>
							{activeTeamMember.position}
						</Text>
					</VStack>
				</VStack>
				<HStack width={"100%"} justifyContent="center" spacing={5}>
					<Button
						width={"20%"}
						backgroundColor="#fffcf8"
						border={"1px solid #532d29"}
						borderRadius="16px"
						ref={node => setPrevEl(node)}
					>
						<BsArrowLeft size={30} color="#532d29" />
					</Button>
					<Button
						width={"20%"}
						backgroundColor="#fffcf8"
						border={"1px solid #d49457"}
						borderRadius="16px"
						ref={node => setNextEl(node)}
					>
						<BsArrowRight size={30} color="#d49457" />
					</Button>
				</HStack>
			</VStack>
		</VStack>
	);
};
