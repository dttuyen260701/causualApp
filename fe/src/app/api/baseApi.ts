import { BaseQueryFn, createApi, FetchArgs, fetchBaseQuery, FetchBaseQueryError } from "@reduxjs/toolkit/query/react";

import { API_URL } from "../../common/utils/config";
//import {IErrorResponse} from "./login/loginType";

const handleErrorSessionTimeOut = (): void => {
	//const defaultUserDataError: IUserInfo = {
	//	userEmail: "",
	//	firstName: "",
	//	lastName: "",
	//	middleName: "",
	//	profilePictureUrl: "",
	//	roleName: "re-login",
	//	userId: "re-login"
	//};
	// store.dispatch(setCurrentUser(defaultUserDataError));
};

const baseQuery = fetchBaseQuery({
	baseUrl: `${API_URL}/api/`,
	prepareHeaders: headers => {
		headers.set("Access-Control-Allow-Origin", "*");
		headers.set("Content-Type", "application/json");
		// const token = sessionStorage.getItem("token");

		//if (token) {
		//	headers.set("Login", `Bearer ${token}`);
		//}
		return headers;
	}
});

const baseQueryWithReAuth: BaseQueryFn<string | FetchArgs, unknown, FetchBaseQueryError> = async (
	args,
	api,
	extraQptions
) => {
	const result = await baseQuery(args, api, extraQptions);
	//if (result.error && (result.error?.data as IErrorResponse).exceptionId === "ExpiredJwtException") {
	//	handleErrorSessionTimeOut();
	//}
	return result;
};

export const baseApi = createApi({
	baseQuery: baseQueryWithReAuth,
	tagTypes: ["Login", "Register"],
	refetchOnMountOrArgChange: true,
	endpoints: () => ({})
});
