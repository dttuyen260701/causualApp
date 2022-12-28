import {
	AlertDialog,
	AlertDialogBody,
	AlertDialogContent,
	AlertDialogFooter,
	AlertDialogOverlay,
	Button,
	Heading,
	useDisclosure,
	Text,
	HStack
} from "@chakra-ui/react";
import React, { useEffect, useRef } from "react";
import { FiAlertTriangle } from "react-icons/fi";

interface IPopupAlert {
	message: string;
	details: string;
	openAlert: boolean;
	handleCloseAlert: (isShowAlert: boolean) => void;
	handleAcceptWarning: () => void;
}

export const PopupAlert = ({
	openAlert,
	handleCloseAlert,
	message,
	details,
	handleAcceptWarning
}: IPopupAlert): JSX.Element => {
	const { isOpen, onOpen, onClose } = useDisclosure();
	const cancelRef = useRef(null);

	useEffect(() => {
		if (openAlert) {
			onOpen();
		}
	}, [onOpen, openAlert]);

	const handleClose = (): void => {
		handleCloseAlert(false);
		onClose();
	};

	return (
		<AlertDialog
			motionPreset="slideInBottom"
			leastDestructiveRef={cancelRef}
			onClose={handleClose}
			isOpen={isOpen}
			isCentered
		>
			<AlertDialogOverlay />

			<AlertDialogContent alignItems="center" padding={"20px"}>
				<FiAlertTriangle color="#f8c486" size={150} />

				<AlertDialogBody>
					<Heading textAlign={"center"} paddingBottom="20px" color={"#595959"} fontSize="30px">
						{message}
					</Heading>
					<Text color={"#595959"}>{details}</Text>
				</AlertDialogBody>
				<AlertDialogFooter width={"50%"} alignItems="center" justifyContent={"center"}>
					<HStack width={"100%"} spacing={5}>
						<Button ref={cancelRef} onClick={handleClose}>
							Cancel
						</Button>
						<Button
							width={"50%"}
							ref={cancelRef}
							onClick={handleAcceptWarning}
							backgroundColor="#00bcd4"
							color={"white"}
						>
							Yes
						</Button>
					</HStack>
				</AlertDialogFooter>
			</AlertDialogContent>
		</AlertDialog>
	);
};
