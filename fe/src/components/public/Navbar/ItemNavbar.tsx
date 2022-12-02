import { Box } from "@chakra-ui/react";
import React from "react";
import { useSelector } from "react-redux";
import { NavLink } from "react-router-dom";

import { getCurrentNameItemNav } from "../../../app/reducer/nameItemNav/nameItemNavSlice";

import { IItemNavbar } from "./navbarTypes";

export const ItemNavbar: React.FC<IItemNavbar> = ({ name, navigate, handleClickItem }) => {
	const selectedItem = useSelector(getCurrentNameItemNav);

	return (
		<NavLink to={navigate}>
			<Box
				color={"#9c7d6e"}
				padding="10px"
				backgroundColor={selectedItem === name ? "#f6d79f" : ""}
				borderRadius={selectedItem === name ? "30px" : ""}
				_hover={{ backgroundColor: "#f6d79f", borderRadius: "30px" }}
				onClick={handleClickItem}
			>
				{name}
			</Box>
		</NavLink>
	);
};
