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
	HStack,
	Input
} from "@chakra-ui/react";
import React, { useEffect, useRef, useState } from "react";
import { RiErrorWarningLine } from "react-icons/ri";

interface IRegisterAlert {
	message: string;
	details: string;
	openAlert: boolean;
	handleCloseAlert: (isShowAlert: boolean) => void;
	handleAcceptAlert: () => void;
	handleGetNote: (note: string) => void;
}

export const JobRegisterAlert = ({
	openAlert,
	handleCloseAlert,
	message,
	details,
	handleAcceptAlert,
	handleGetNote
}: IRegisterAlert): JSX.Element => {
	const { isOpen, onOpen, onClose } = useDisclosure();
	const cancelRef = useRef(null);
	const [note, setNote] = useState<string>("");

	useEffect(() => {
		if (openAlert) {
			onOpen();
		}
	}, [onOpen, openAlert]);

	const handleClose = (): void => {
		handleCloseAlert(false);
		onClose();
	};

	const handleChangeNote = (e: React.ChangeEvent<HTMLInputElement>) => {
		setNote(e.target.value);
		handleGetNote(e.target.value);
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
				<RiErrorWarningLine color="#f8c486" size={150} />
				<AlertDialogBody>
					<Heading textAlign={"center"} paddingBottom="20px" color={"#595959"} fontSize="30px">
						{message}
					</Heading>
					<Text color={"#595959"}>{details}</Text>
					<Input
						focusBorderColor={"none"}
						placeholder={"Ghi chú cho công việc"}
						borderRadius={"15px"}
						maxLength={50}
						borderColor={"primary"}
						_focus={{
							borderColor: "#f9d475",
							borderWidth: "2px"
						}}
						_hover={{
							shadow: "md",
							borderColor: "#f9d475",
							boxShadow: "0px 0px 13px -3px #FDB493"
						}}
						value={note}
						onChange={handleChangeNote}
					/>
				</AlertDialogBody>
				<AlertDialogFooter width={"50%"} alignItems="center" justifyContent={"center"}>
					<HStack width={"100%"} spacing={5}>
						<Button ref={cancelRef} onClick={handleClose}>
							Cancel
						</Button>
						<Button width={"50%"} ref={cancelRef} onClick={handleAcceptAlert} backgroundColor="#00bcd4" color={"white"}>
							Yes
						</Button>
					</HStack>
				</AlertDialogFooter>
			</AlertDialogContent>
		</AlertDialog>
	);
};
