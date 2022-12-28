import {
	AlertDialog,
	AlertDialogBody,
	AlertDialogContent,
	AlertDialogFooter,
	AlertDialogOverlay,
	Button,
	Heading,
	useDisclosure,
	Text
} from "@chakra-ui/react";
import React, { useEffect, useRef } from "react";
import { TiTick } from "react-icons/ti";
import { VscError } from "react-icons/vsc";

interface IPopup {
	typeAlert: string;
	message: string;
	details: string;
	openAlert: boolean;
	handleCloseAlert: (isShowAlert: boolean) => void;
}

export const Popup = ({ openAlert, handleCloseAlert, message, details, typeAlert }: IPopup): JSX.Element => {
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
			onClose={onClose}
			isOpen={isOpen}
			isCentered
		>
			<AlertDialogOverlay />

			<AlertDialogContent alignItems="center" padding={"20px"}>
				{typeAlert === "success" ? (
					<TiTick color="#f8c486" size={150} />
				) : typeAlert === "error" ? (
					<VscError color="red" size={150} />
				) : (
					<></>
				)}

				<AlertDialogBody>
					<Heading textAlign={"center"} paddingBottom="20px" color={"#595959"} fontSize="30px">
						{message}
					</Heading>
					<Text color={"#595959"} textAlign="center">
						{details}
					</Text>
				</AlertDialogBody>
				<AlertDialogFooter width={"50%"} alignItems="center" justifyContent={"center"}>
					<Button width={"50%"} ref={cancelRef} onClick={handleClose} backgroundColor="#f9d475" color={"#000000"}>
						OK
					</Button>
				</AlertDialogFooter>
			</AlertDialogContent>
		</AlertDialog>
	);
};
