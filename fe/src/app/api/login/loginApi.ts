import { baseApi } from "../baseApi";

import { ILoginResponse, IUserLoginInput } from "./loginType";

export const loginApi = baseApi.injectEndpoints({
	endpoints: builder => ({
		login: builder.mutation<ILoginResponse, IUserLoginInput>({
			query: body => {
				return {
					url: "app/account/login",
					method: "post",
					headers: {
						"content-type": "application/json"
					},
					body: body
				};
			},
			invalidatesTags: ["Login"]
		})
		//queryCurrentUser: builder.query<IUserInfo, void>({
		//	query: () => `/users/current-user`
		//})
	})
});

export const {
	useLoginMutation
	// useLazyQueryCurrentUserQuery
} = loginApi;
