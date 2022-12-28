import {baseApi} from "../../baseApi";

import {IGetAllJobResponse} from "./types";

export const typeOfJobApi = baseApi.injectEndpoints({
	endpoints: builder => ({
		getAllTypeOfJob: builder.query<IGetAllJobResponse, void>({
			query: () => `type-of-job/all-type`,
			providesTags: ['TypeOfJob']
		}),
	})
});

export const {useGetAllTypeOfJobQuery} = typeOfJobApi;