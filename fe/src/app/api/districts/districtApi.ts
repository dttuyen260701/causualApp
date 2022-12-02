import {baseApi} from "../../baseApi";

import {IDistrict} from "./districtTypes";

export const districtApi = baseApi.injectEndpoints({
	endpoints: builder => ({
		getDistrictByIdProvince: builder.query<IDistrict[], string>({
			query: provinceId => `address/${provinceId}/districts`,
			providesTags: ['District']
		})
	})
})

export const { useGetDistrictByIdProvinceQuery } = districtApi;