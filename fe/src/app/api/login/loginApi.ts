import { baseApi } from "../../baseApi";

import { ILoginResponse, IUserLoginInput } from "./loginType";

export const loginApi = baseApi.injectEndpoints({
	endpoints: builder => ({
		login: builder.mutation<ILoginResponse, IUserLoginInput>({
			query: body => {
				return {
					url: "account/login",
					method: "post",
					headers: {
						"content-type": "application/json"
					},
					body: body
				};
			},
			invalidatesTags: ["Login"]
		})
	})
});

export const {
	useLoginMutation
} = loginApi;
