import React, { useRef } from "react";
import { Box, Button, HStack, Text } from "@chakra-ui/react";
import { BiChevronsLeft, BiChevronsRight } from "react-icons/bi";

import { IPagination } from "./types";
import { DOTS, usePagination } from "./usePagination";

const Pagination: React.FC<IPagination> = props => {
	const ref = useRef<HTMLInputElement>(null);
	const paginationRange = usePagination(props);

	if (props.currentPage === 0 || (paginationRange && paginationRange.length < 1)) {
		return null;
	}
	const onNext = () => {
		props.onPageChange(props.currentPage + 1);
	};

	const onPrevious = () => {
		props.onPageChange(props.currentPage - 1);
	};

	const scrollToBottom = () => {
		setTimeout(() => {
			if (ref.current) {
				ref.current?.scrollIntoView({ behavior: "smooth" });
			}
		}, 300);
	};

	const lastPage = paginationRange ? paginationRange[paginationRange.length - 1] : 0;
	return (
		<HStack justifyContent={"flex-end"} paddingBottom="24px" spacing={"4px"} ref={ref}>
			<Button
				onClick={() => {
					if (props.currentPage > 1) {
						scrollToBottom();
						onPrevious();
					}
				}}
				width={{ base: "32px", md: "80px" }}
				height="32px"
				text-align="center"
				display="flex"
				boxSizing="border-box"
				alignItems="center"
				letterSpacing="0.01071em"
				borderRadius="16px"
				lineHeight="1.43"
				fontSize="14px"
				isDisabled={props.currentPage === 1}
				_hover={{
					boxShadow: "8px 8px 10px rgb(249, 212, 117, 0.6)"
				}}
				bgColor="#f9d475"
				cursor="pointer"
			>
				<HStack spacing={{ base: 0, md: "8px" }}>
					<BiChevronsLeft fontSize={"20px"} />
					<Text display={{ md: "flex", base: "none" }}>Prev</Text>
				</HStack>
			</Button>

			{paginationRange?.map((pageNumber, index) => {
				if (pageNumber === DOTS) {
					return (
						<Box key={index} _hover={{ bgColor: "transparent" }}>
							&#8230;
						</Box>
					);
				}
				return (
					<Box
						key={index}
						onClick={() => {
							scrollToBottom();
							props.onPageChange(pageNumber as number);
						}}
						padding="0 12px"
						height="32px"
						text-align="center"
						display="flex"
						boxSizing="border-box"
						alignItems="center"
						letterSpacing="0.01071em"
						borderRadius="16px"
						lineHeight="1.43"
						fontSize="16px"
						minWidth="32px"
						color={props.currentPage === pageNumber ? "#000000" : "#9c7d6e"}
						background={"transparent"}
						_hover={{
							bgColor: "primary"
						}}
						cursor="pointer"
					>
						{pageNumber}
					</Box>
				);
			})}
			<Button
				onClick={() => {
					if (props.currentPage < lastPage) {
						scrollToBottom();
						onNext();
					}
				}}
				width={{ base: "32px", md: "80px" }}
				padding={"0"}
				height="32px"
				text-align="center"
				display="flex"
				boxSizing="border-box"
				alignItems="center"
				letterSpacing="0.01071em"
				borderRadius="16px"
				lineHeight="1.43"
				fontSize="14px"
				isDisabled={props.currentPage === lastPage}
				_hover={{
					boxShadow: "8px 8px 10px rgb(249, 212, 117, 0.6)"
				}}
				bgColor="#f9d475"
				cursor="pointer"
			>
				<HStack spacing={{ base: 0, md: "8px" }}>
					<Text display={{ md: "flex", base: "none" }}>Next</Text>
					<BiChevronsRight fontSize={"20px"} />
				</HStack>
			</Button>
		</HStack>
	);
};

export default Pagination;
