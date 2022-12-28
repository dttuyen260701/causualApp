import React from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import SwiperCore, { Navigation, Pagination, EffectCoverflow } from "swiper";

// Import Swiper styles
import "swiper/swiper.min.css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import "swiper/css/effect-coverflow";
import { Card } from "../../../common/shared-components/CardJobType/Card";
import { useGetAllTypeOfJobQuery } from "../../../app/api/typeOfJob/typeOfJobApi";

SwiperCore.use([Navigation, Pagination, EffectCoverflow]);

export const TypeOfJob: React.FC = () => {
	const { data: dataTypeOfJob } = useGetAllTypeOfJobQuery();

	return (
		<Swiper
			navigation
			pagination={{ clickable: true }}
			effect="coverflow"
			coverflowEffect={{
				rotate: 0,
				stretch: 0,
				depth: 50,
				modifier: 2.5,
				slideShadows: true
			}}
			slidesPerView={3}
			centeredSlides={true}
			loop={true}
			grabCursor={true}
			style={{ height: "500px", width: "90vw" }}
		>
			{dataTypeOfJob &&
				dataTypeOfJob?.resultObj.map((item, index) => (
					<SwiperSlide key={index}>
						<Card
							key={item.id}
							cardFront={{ title: item.name, image: item.image }}
							cardBack={{ title: item.name, description: item.des }}
						/>
					</SwiperSlide>
				))}
		</Swiper>
	);
};
