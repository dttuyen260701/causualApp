import { CSSProperties } from "react";
import { StylesConfig } from "react-select";
import { useColorMode } from "@chakra-ui/react";
//import { useSelector } from "react-redux";

//import { getCurrentExpandSidebar } from "../../app/reducers/expandSidebar/expandSidebarSlice";

import { IOption } from "../shared-components/Select/SelectTypes";
//import { useWindowResize } from "../hooks/useWindowResize";

const customControlStyles: CSSProperties = {
	borderWidth: 0,
	height: "40px"
};
const customColor: CSSProperties = {
	color: "#FFFFFF"
};

interface IInputSelectProps {
	style?: CSSProperties;
}

const useInputSelectStyle = ({ style }: IInputSelectProps) => {
	const { colorMode } = useColorMode();
	//const { width } = useWindowResize();
	//const isExpand: boolean = useSelector(getCurrentExpandSidebar);
	//const screenWidth = width ?? 0;
	//const menuListWidth =
	//	screenWidth <= 768
	//		? "100%"
	//		: screenWidth > 768 && screenWidth < 1100
	//		? "150%"
	//		: screenWidth < 1200 && isExpand
	//		? "120%"
	//		: "100%";

	const selectStyle: StylesConfig<IOption, false> = {
		option: (provided, state) => {
			return {
				...provided,
				...customControlStyles,
				"&:hover": {
					backgroundColor: colorMode === "dark" ? "rgba(74,85,104,0.5)" : "#EDF2F7"
				},
				color: colorMode === "dark" ? "#FFFFFF" : "black",
				borderRadius: 0,
				fontSize: 16,
				display: "flex",
				alignItems: "center",
				paddingLeft: "32px",
				backgroundColor: state.isFocused ? (colorMode === "dark" ? "rgba(74,85,104,0.5)" : "#EDF2F7") : "",
				lineHeight: "24px",
				height: "36.8px",
				position: "relative",
				"&::before": {
					content: `""`,
					height: "10px",
					width: "6px",
					borderRightWidth: "2.2px",
					borderBottomWidth: "2.2px",
					borderColor: colorMode === "dark" ? "#FFFFFF" : "black",
					transform: "rotate(45deg)",
					position: "absolute",
					visibility: state.isSelected ? "visible" : "hidden",
					left: "13px"
				}
			};
		},
		control: provided => {
			return {
				...provided,
				...customControlStyles,
				...style,
				cursor: "pointer",
				"&:hover": {
					borderWidth: 0
				},
				backgroundColor: "transparent",
				color: colorMode === "dark" ? "#FFFFFF" : "black",
				borderRadius: 6,
				fontSize: 16,
				paddingLeft: "8px",
				borderWidth: 0,
				boxShadow: "none",
				width: "100%"
			};
		},
		menuList: provided => {
			return {
				...provided,
				paddingTop: "8px",
				paddingBottom: "8px",
				overflowX: "hidden",
				color: colorMode === "dark" ? "#FFFFFF" : "black",
				borderRadius: 6,
				//width: menuListWidth,
				width: "100%",
				backgroundColor: colorMode === "dark" ? "#2D3748" : "#FFFFFF",
				"&::-webkit-scrollbar": {
					width: "4px"
				},
				"&::-webkit-scrollbar-track": {
					width: "6px"
				},
				"&::-webkit-scrollbar-thumb": {
					background: "#6a5af9",
					borderRadius: "24px"
				}
			};
		},
		input: provided => {
			return {
				...provided,
				...customColor,
				color: colorMode === "dark" ? "#FFFFFF" : "black",
				borderWidth: 0
			};
		},
		placeholder: provided => {
			return {
				...provided,
				...customColor,
				color: colorMode === "dark" ? "#FFFFFF" : "black"
			};
		},
		singleValue: provided => {
			return {
				...provided,
				...customColor,
				color: colorMode === "dark" ? "#FFFFFF" : "black"
			};
		},

		indicatorSeparator: base => ({
			...base,
			display: "none"
		}),
		dropdownIndicator: base => ({
			...base,
			color: colorMode === "dark" ? "#f9d475" : "black",
			paddingRight: "16px",
			svg: {
				height: "1em",
				width: "1em",
				"&:hover": {
					color: colorMode === "dark" ? "#f9d475" : "black"
				}
			}
		}),
		indicatorsContainer: base => ({
			...base,
			"&:hover": {
				svg: {
					color: colorMode === "dark" ? "#f9d475" : "black"
				}
			}
		})
	};
	return {
		selectStyle
	};
};

export default useInputSelectStyle;
