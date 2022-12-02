import {baseApi} from "../../baseApi";

import {IProvince} from "./provinceType";

export const provinceApi = baseApi.injectEndpoints({
	endpoints: builder => ({
		getAllProvinces: builder.query<IProvince[], void>({
			query: () => `address/provinces`,
			providesTags: ['Province']
		})
	})
})

export const {useGetAllProvincesQuery} = provinceApi;