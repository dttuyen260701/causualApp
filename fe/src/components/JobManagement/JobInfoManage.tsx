import {
	Heading,
	HStack,
	Input,
	InputGroup,
	InputLeftElement,
	VStack,
	Text,
	Menu,
	MenuButton,
	Box,
	Img,
	MenuList,
	MenuItem,
	useMediaQuery,
	Avatar
} from "@chakra-ui/react";
import React, { useEffect, useState } from "react";
import { BiSearchAlt } from "react-icons/bi";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { AiOutlineInfoCircle } from "react-icons/ai";

import { JobRegisterCard } from "../../common/shared-components/JobCard/JobRegisterCard";
import { JobInfoCard } from "../../common/shared-components/JobCard/JobInfoCard";
import {
	useDeleteJobInfoOfWorkerMutation,
	useGetAllJobInfoQuery,
	useGetJobInfoOfWorkerQuery,
	useRegisterJobForWorkerMutation
} from "../../app/api/jobManageWorker/jobInfoApi";
import { getCurrentWorker } from "../../app/reducer/loginAuth/loginAuthSlice";
import { JobRegisterAlert } from "../../common/shared-components/JobCard/JobRegisterAlert";
import { API_URL } from "../../common/utils/config";
import { Popup } from "../../common/shared-components/Popup/Popup";
import { IDeleteJobInfoInput } from "../../app/api/jobManageWorker/types";
import { PopupAlert } from "../../common/shared-components/Popup/PopupAlert";
import useDebounce from "../../common/hooks/useDebounce";
import { Loading } from "../../common/shared-components/Loading/Loading";
import { LoadingOverlay } from "../../common/shared-components/Loading/LoadingOverLay";

export const JobInfoManage: React.FC = () => {
	const [showSideBar] = useMediaQuery("(min-width: 1100px)");
	const navigate = useNavigate();
	const resultObj = useSelector(getCurrentWorker);
	const [search, setSearch] = useState<string>("");
	const debouncedSearch = useDebounce(search, 500);
	const { data: dataAllJobInfo, refetch: refetchAllJobInfo, isLoading: isLoadingAllJobInfo } = useGetAllJobInfoQuery({
		workerId: resultObj.id,
		searchText: debouncedSearch !== "" ? debouncedSearch : ""
	});
	const {
		data: dataJobInfoWorker,
		refetch: refetchDataJobInfo,
		isLoading: isLoadingDataJobInfo
	} = useGetJobInfoOfWorkerQuery(resultObj.id);
	const [noteJob, setNoteJob] = useState<string>("");
	const [registerJob, { isLoading: isRegistering }] = useRegisterJobForWorkerMutation();
	const [deleteJob, { isLoading: isDeleting }] = useDeleteJobInfoOfWorkerMutation();
	const [isShowAlertRegister, setIsShowAlertRegister] = useState<boolean>(false);
	const [jobActionInput, setJobActionInput] = useState<IDeleteJobInfoInput>({ workerId: "", jobInfoId: "" });

	const [isShowSuccessPopup, setIsShowSuccessPopup] = useState<boolean>(false);
	const [isShowErrorPopup, setIsShowErrorPopup] = useState<boolean>(false);
	const [isShowPopupWarn, setIsShowPopupWarn] = useState<boolean>(false);
	const [messagePopup, setMessagePopup] = useState({ message: "", detail: "" });
	const [nameAvatar, setNameAvatar] = useState("");

	useEffect(() => {
		refetchAllJobInfo();
	}, [debouncedSearch, refetchAllJobInfo]);

	useEffect(() => {
		const data = resultObj.name.split(" ");
		data.length > 1 ? setNameAvatar(data[0] + " " + data[data.length - 1]) : setNameAvatar(data[0]);
	}, [resultObj.name]);

	const handleRegisterJob = (jobId: string) => {
		setMessagePopup({
			...messagePopup,
			message: "Đăng ký việc làm",
			detail: "Bạn chắc chắn muốn đăng ký công việc này?"
		});
		setIsShowAlertRegister(true);
		setJobActionInput({ ...jobActionInput, jobInfoId: jobId });
	};

	const handleAcceptAlertRegister = () => {
		registerJob({
			userId: resultObj.id,
			jobInfoId: jobActionInput.jobInfoId,
			note: noteJob
		})
			.unwrap()
			.then(res => {
				if (res.isSuccessed) {
					setIsShowAlertRegister(false);
					setMessagePopup({
						...messagePopup,
						message: "Đăng ký thành công",
						detail: "Bạn đã đăng ký thành công công việc này"
					});
					setIsShowSuccessPopup(true);
					refetchDataJobInfo();
					refetchAllJobInfo();
				}
			})
			.catch(error => {
				setMessagePopup({
					...messagePopup,
					message: "ERROR",
					detail: error
				});
				setIsShowErrorPopup(true);
			});
	};

	const handleDeleteJob = (jobInfo: IDeleteJobInfoInput) => {
		setMessagePopup({
			...messagePopup,
			message: "Xoá việc làm",
			detail: "Bạn chắc chắn muốn xoá công việc này?"
		});
		setIsShowPopupWarn(true);
		setJobActionInput(jobInfo);
	};

	const handleCloseAlert = (isShowAlert: boolean) => {
		setIsShowAlertRegister(isShowAlert);
	};

	const handleAcceptWarning = () => {
		deleteJob({
			workerId: jobActionInput.workerId,
			jobInfoId: jobActionInput.jobInfoId
		})
			.unwrap()
			.then(res => {
				if (res.isSuccessed) {
					setIsShowPopupWarn(false);
					setMessagePopup({
						...messagePopup,
						message: "Xoá thành công",
						detail: "Bạn đã xoá thành công công việc này"
					});
					setIsShowSuccessPopup(true);
					refetchAllJobInfo();
					refetchDataJobInfo();
				}
			})
			.catch(error => {
				setMessagePopup({
					...messagePopup,
					message: "ERROR",
					detail: error
				});
				setIsShowErrorPopup(true);
			});
	};

	return (
		<VStack width={"100%"} height="100%" spacing={10}>
			<HStack width={"100%"} justifyContent="space-between" paddingRight={"40px"} paddingLeft="30px">
				<Heading color={"#000000"}>Quản lý công việc</Heading>
				<HStack spacing={4}>
					<Text fontSize={"16px"} fontWeight="600">
						{resultObj.name}
					</Text>
					<Menu>
						<MenuButton>
							<Box
								w={showSideBar ? "60px" : "50px"}
								h={showSideBar ? "60px" : "50px"}
								minWidth={showSideBar ? "40px" : "30px"}
							>
								{resultObj.avatar !== "" ? (
									<Img src={`${API_URL}${resultObj.avatar}`} w="100%" h="100%" objectFit="cover" borderRadius="50px" />
								) : (
									<Avatar
										name={nameAvatar}
										size="10px"
										w="100%"
										h="100%"
										objectFit="cover"
										borderRadius="50px"
										bgColor={"#fefcf6"}
										color="#1a202c"
										border={"1px solid #d59457"}
									/>
								)}
							</Box>
						</MenuButton>
						<MenuList>
							{/*<MenuItem isDisabled>{`${name} ${surname}`}</MenuItem>
							<MenuItem isDisabled>{email}</MenuItem>
							<MenuDivider />*/}
							<MenuItem color="#532d29" onClick={() => navigate("/update-info")}>
								Cập nhật thông tin
								<HStack marginLeft={"50px"}>
									<AiOutlineInfoCircle cursor="pointer" fontSize="16px" />
								</HStack>
							</MenuItem>
						</MenuList>
					</Menu>
				</HStack>
			</HStack>
			<VStack width={"100%"} spacing={5}>
				<Heading width={"100%"} textAlign="left" fontWeight={700} fontSize="20px" paddingLeft={"20px"} color="#f9d475">
					Các công việc của bạn
				</Heading>
				{isLoadingDataJobInfo ? (
					<Loading />
				) : (
					<VStack width={"100%"} maxH="50vh" overflow={"scroll"} spacing={5} padding="10px 0px">
						{dataJobInfoWorker?.resultObj.length !== 0 ? (
							dataJobInfoWorker?.resultObj.map((item, index) => (
								<JobInfoCard
									key={index}
									jobInfo={item}
									handleClickRegister={() => {
										return null;
									}}
									handleDeleteJobInfo={handleDeleteJob}
								/>
							))
						) : (
							<Text color={"#d49457"} width={"100%"} textAlign="center" fontSize={"18px"}>
								Hiện tại bạn không có công việc nào
							</Text>
						)}
					</VStack>
				)}
			</VStack>
			<VStack width={"100%"} spacing={5}>
				<HStack width={"100%"} justifyContent="space-between">
					<Heading
						width={"100%"}
						textAlign="left"
						fontWeight={700}
						fontSize="20px"
						paddingLeft={"20px"}
						color="#f9d475"
					>
						Một số gợi ý công việc khác dành cho bạn
					</Heading>
					<InputGroup width={"50%"} paddingRight="30px">
						<InputLeftElement pointerEvents="none">
							<BiSearchAlt />
						</InputLeftElement>
						<Input
							borderRadius={"20px"}
							borderColor={"#d0c5ba"}
							focusBorderColor={"none"}
							_focus={{
								borderColor: "#f0d4ba",
								borderWidth: "2px"
							}}
							_hover={{
								shadow: "md",
								borderColor: "#f0d4ba",
								boxShadow: "0px 0px 13px -3px #f0d4ba"
							}}
							placeholder="Tìm kiếm công việc theo tên"
							value={search}
							onChange={(e: React.ChangeEvent<HTMLInputElement>) => setSearch(e.target.value)}
						/>
					</InputGroup>
				</HStack>

				{isLoadingAllJobInfo ? (
					<Loading />
				) : (
					<VStack width={"100%"} maxH="50vh" overflow={"scroll"} spacing={2}>
						{dataAllJobInfo?.resultObj.length !== 0 &&
							dataAllJobInfo?.resultObj.map(item => (
								<JobRegisterCard
									key={item.id}
									jobInfo={item}
									handleClickRegister={handleRegisterJob}
									handleDeleteJobInfo={() => {
										return null;
									}}
								/>
							))}
					</VStack>
				)}
			</VStack>
			{isShowAlertRegister && (
				<JobRegisterAlert
					openAlert={isShowAlertRegister}
					message={messagePopup.message}
					details={messagePopup.detail}
					handleCloseAlert={handleCloseAlert}
					handleAcceptAlert={handleAcceptAlertRegister}
					handleGetNote={(note: string) => {
						setNoteJob(note);
					}}
				/>
			)}
			{isShowSuccessPopup && (
				<Popup
					typeAlert="success"
					openAlert={isShowSuccessPopup}
					message={messagePopup.message}
					details={messagePopup.detail}
					handleCloseAlert={(isShowPopup: boolean) => setIsShowSuccessPopup(isShowPopup)}
				/>
			)}
			{isShowErrorPopup && (
				<Popup
					typeAlert="error"
					openAlert={isShowErrorPopup}
					message={messagePopup.message}
					details={messagePopup.detail}
					handleCloseAlert={(isShowPopup: boolean) => setIsShowErrorPopup(isShowPopup)}
				/>
			)}
			{isShowPopupWarn && (
				<PopupAlert
					openAlert={isShowPopupWarn}
					message={messagePopup.message}
					details={messagePopup.detail}
					handleCloseAlert={(isPopupWarn: boolean) => setIsShowPopupWarn(isPopupWarn)}
					handleAcceptWarning={handleAcceptWarning}
				/>
			)}
			{isRegistering && <LoadingOverlay openLoading={isRegistering} />}
			{isDeleting && <LoadingOverlay openLoading={isDeleting} />}
		</VStack>
	);
};
