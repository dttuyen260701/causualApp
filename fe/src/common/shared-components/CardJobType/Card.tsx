import { Box, Img, VStack, Text, HStack, Heading } from "@chakra-ui/react";
import React from "react";
import "../../../App.css";

import { CardBack } from "./CardBack";
import { CardFront } from "./CardFront";
import { ICard } from "./types";

export const Card: React.FC<ICard> = ({ cardFront, cardBack }) => {
	return (
		<div className="card-container">
			<div className="card-body">
				<CardFront title={cardFront.title} image={cardFront.image} />
				<CardBack title={cardBack.title} description={cardBack.description} />
			</div>
		</div>
	);
};
