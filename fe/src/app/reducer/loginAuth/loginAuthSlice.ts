import {createSlice, PayloadAction} from "@reduxjs/toolkit";

import {ILoginResponse, IUserInfo} from "../../api/login/loginType";
import {RootState} from "../../store";

export const user = localStorage.getItem("user") !== null ? JSON.parse(localStorage.getItem("user") ?? '') as IUserInfo : null;

export const initialState: ILoginResponse = {
	isSuccessed: false,
	message: '',
	resultObj: {
		id: user?.id ?? '',
    	email: user?.email ?? '',
    	userName: user?.userName ?? '',
    	phone: user?.phone ?? 0,
    	name: user?.name ?? '',
    	identityCard: user?.identityCard ?? '',
    	identityCardDate: user?.identityCardDate ?? '',
    	identityCardBy: user?.identityCardBy ?? '',
    	gender: user?.gender ?? 0,
    	dateOfBirth: user?.dateOfBirth ?? '',
    	startWorkingTime: user?.startWorkingTime ?? '',
    	endWorkingTime: user?.endWorkingTime ?? '',
    	provinceId: user?.provinceId ?? '',
    	provinceName: user?.provinceName ?? '',
    	districtId: user?.districtId ?? '',
    	districtName: user?.districtName ?? '',
    	wardId: user?.wardId ?? '',
    	wardName: user?.wardName ?? '',
    	address: user?.address ?? '',
    	addressPoint: user?.addressPoint ?? '',
    	role: user?.addressPoint ?? '',
    	avatar: user?.avatar ?? '',
    	lastModificationTime: user?.lastModificationTime ?? ''
	}
};

export const loginAuthSlice = createSlice({
	name: "loginAuth",
	initialState,
	reducers: {
		setCurrentWorker: (state: ILoginResponse, action: PayloadAction<IUserInfo>) => {
			state.resultObj = action.payload;
		},
		logout: state => {
			state.resultObj = initialState.resultObj;
			localStorage.removeItem("user");
		}
	}
});
export const { logout, setCurrentWorker } = loginAuthSlice.actions;
export const getCurrentWorker = (state: RootState) => state.loginAuth.resultObj;
export default loginAuthSlice.reducer;