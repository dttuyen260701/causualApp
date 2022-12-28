import { AlertDialog, AlertDialogContent, AlertDialogOverlay, useDisclosure } from "@chakra-ui/react";
import React, { useEffect, useRef } from "react";

import { Loading } from "./Loading";

interface ILoadingOverlay {
	openLoading: boolean;
	handleCloseLoading?: (isShow: boolean) => void;
}

export const LoadingOverlay = ({ openLoading }: ILoadingOverlay): JSX.Element => {
	const { isOpen, onOpen, onClose } = useDisclosure();
	const cancelRef = useRef(null);

	useEffect(() => {
		if (openLoading) {
			onOpen();
		} else {
			onClose();
		}
	}, [onClose, onOpen, openLoading]);

	return (
		<AlertDialog
			motionPreset="slideInBottom"
			leastDestructiveRef={cancelRef}
			onClose={onClose}
			isOpen={isOpen}
			isCentered
		>
			<AlertDialogOverlay />

			<AlertDialogContent alignItems="center" bgColor={"transparent"} border="none" boxShadow={"none"} padding={"20px"}>
				<Loading />
			</AlertDialogContent>
		</AlertDialog>
	);
};
