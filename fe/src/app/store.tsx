import { configureStore } from "@reduxjs/toolkit";
import { setupListeners } from "@reduxjs/toolkit/dist/query";

import nameItemNavReducer from "./reducer/nameItemNav/nameItemNavSlice";
import expandSidebarReducer from "./reducer/expandSidebar/expandSidebarSlice";
import nameItemSideBarReducer from "./reducer/nameItemSideBar/nameItemSideBarSlice";
import loginAuthReducer from "./reducer/loginAuth/loginAuthSlice";
import { baseApi } from "./baseApi";

export const store = configureStore({
	reducer: {
		nameItemNav: nameItemNavReducer,
		expandSidebar: expandSidebarReducer,
		nameItemSideBar: nameItemSideBarReducer,
		loginAuth: loginAuthReducer,
		[baseApi.reducerPath]: baseApi.reducer
	},
	middleware: getDefaultMiddleware => getDefaultMiddleware().concat(baseApi.middleware)
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

setupListeners(store.dispatch);
