import {baseApi} from "../../baseApi";

import {IRegisterResponse, IUserInputRegister } from "./registerType";

export const registerApi = baseApi.injectEndpoints({
	endpoints: builder => ({
		register: builder.mutation<IRegisterResponse, IUserInputRegister>({
			query: body => {
				return {
					url: "account/register-as-worker",
					method: "post",
					headers: {
						"content-type": "application/json"
					},
					body: body
				}
			},
			invalidatesTags: ["Register"]
		})
	})
});

export const {useRegisterMutation} = registerApi;