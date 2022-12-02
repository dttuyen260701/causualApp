import { HStack } from "@chakra-ui/react";
import React from "react";
import { MdSystemSecurityUpdateGood } from "react-icons/md";
import { GoGraph } from "react-icons/go";
import { GiVibratingShield } from "react-icons/gi";

import { FeatureCard } from "../../../common/shared-components/FeatureCard/FeatureCard";

export const FeatureCardGroup: React.FC = () => {
	return (
		<HStack width={"100%"} justifyContent="center" alignItems={"center"} spacing={5} paddingTop={"40px"}>
			<FeatureCard content="Đặt lịch nhanh chóng" icon={<MdSystemSecurityUpdateGood color="#d49457" size={"30"} />} />
			<FeatureCard content="Xếp hạng chất lượng thợ" icon={<GiVibratingShield color="#d49457" size={"30"} />} />
			<FeatureCard content="Thống kê doanh thu" icon={<GoGraph color="#d49457" size={"30"} />} />
		</HStack>
	);
};
