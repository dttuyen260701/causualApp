import { configureStore } from "@reduxjs/toolkit";
import { setupListeners } from "@reduxjs/toolkit/dist/query";

import nameItemNavReducer from "./reducer/nameItemNav/nameItemNavSlice";
import { baseApi } from "./baseApi";

export const store = configureStore({
	reducer: {
		nameItemNav: nameItemNavReducer,
		[baseApi.reducerPath]: baseApi.reducer
	},
	middleware: getDefaultMiddleware => getDefaultMiddleware().concat(baseApi.middleware)
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

setupListeners(store.dispatch);