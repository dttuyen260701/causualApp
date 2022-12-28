import { BaseQueryFn, createApi, FetchArgs, fetchBaseQuery, FetchBaseQueryError } from "@reduxjs/toolkit/query/react";

import { API_URL } from "../common/utils/config";

const baseQuery = fetchBaseQuery({
	baseUrl: `${API_URL}/api/app/`,
	prepareHeaders: headers => {
		headers.set("Access-Control-Allow-Origin", "*");
		return headers;
	}
});

const baseQueryWithReAuth: BaseQueryFn<string | FetchArgs, unknown, FetchBaseQueryError> = async (
	args,
	api,
	extraQptions
) => {
	const result = await baseQuery(args, api, extraQptions);
	return result;
};

export const baseApi = createApi({
	baseQuery: baseQueryWithReAuth,
	tagTypes: ["Login", "Register","Province",
	'District', "Ward", 'TypeOfJob', "JobInfo", 
	"JobStatistic", "JobInfoWorker", "JobRegister", 
	"JobDelete", "OrderList", "JobTotal", "RateWorker", "updateWorker", "changePass"],
	refetchOnMountOrArgChange: true,
	endpoints: () => ({})
});
