import {baseApi} from "../../baseApi";

import {IWard} from "./wardTypes";

export const wardApi = baseApi.injectEndpoints({
	endpoints: builder => ({
		getWardByIdDistrict: builder.query<IWard[], string>({
			query: districtId => `address/${districtId}/wards`,
			providesTags: ["Ward"]
		})
	})
})

export const {useGetWardByIdDistrictQuery} = wardApi;