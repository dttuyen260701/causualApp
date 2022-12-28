import { createSlice } from "@reduxjs/toolkit";
import type { PayloadAction } from "@reduxjs/toolkit";

import { RootState } from "../../store";

import {INameItemNav} from "./types";


export const initialState: INameItemNav = {
	name: "Trang chủ"
};
export const nameItemNavSlice = createSlice({
	name: "nameItemNav",
	initialState,
	reducers: {
		setNameItemNav: (state: INameItemNav, action: PayloadAction<string>) => {
			state.name = action.payload;
		}
	}
});
export const { setNameItemNav } = nameItemNavSlice.actions;
export const getCurrentNameItemNav = (state: RootState) => state.nameItemNav.name;
export default nameItemNavSlice.reducer;
