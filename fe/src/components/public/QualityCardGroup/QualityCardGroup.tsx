import { HStack, VStack } from "@chakra-ui/react";
import React from "react";

import { QualityCard } from "../../../common/shared-components/QualityCard/QualityCard";

export const QualityCardGroup: React.FC = () => {
	return (
		<VStack spacing={10} padding="50px 50px" width={"100%"} height="60vh">
			<HStack spacing={10}>
				<QualityCard
					title="Đặt lịch nhanh chóng"
					content="Thao tác 60 giây trên ứng dụng, có ngay người nhận việc sau 60 phút"
				/>
				<QualityCard
					title="Giá cả rõ ràng"
					content="Giá dịch vụ được hiển thị rõ ràng trên ứng dụng. Bạn không phải trả thêm bất kỳ khoản chi phí nào."
				/>
			</HStack>
			<HStack spacing={10}>
				<QualityCard
					title="Đa dạng dịch vụ"
					content="Với các dịch vụ tiện ích, chúng tôi sẵn sàng hỗ trợ mọi nhu cầu việc nhà của bạn."
				/>
				<QualityCard
					title="An toàn tối đa"
					content="Người làm uy tín, luôn có hồ sơ lý lịch rõ ràng và được Công ty giám sát trong suốt quá trình làm việc."
				/>
			</HStack>
		</VStack>
	);
};
