import { baseApi } from "../../baseApi";

import {IChangePassInput, IChangePassRes, IUpdateUserRes, IWorkerInfoUpdateInput} from "./types";


export const updateWorkerApi = baseApi.injectEndpoints({
	endpoints: builder => ({
		updateWorker: builder.mutation<IUpdateUserRes, IWorkerInfoUpdateInput>({
			query(body) {
				
				return {
					url: `account/edit-worker-info/${body.workerId}`,
					method: "put",
					body: body.formdata
				};
			},
			invalidatesTags: ["updateWorker"]
		}),
		changePassword: builder.mutation<IChangePassRes, IChangePassInput>({
			query: body => {
				return {
					url: `account/change-password/${body.idUser}`,
					method: 'post',
					headers: {
						"content-type": "application/json"
					},
					body: {
						newPassword: body.newPassword,
						oldPassword: body.oldPassword
					}
				}
			},
			invalidatesTags: ["changePass"]
		})
	})
});

export const {
	useUpdateWorkerMutation,
	useChangePasswordMutation,
} = updateWorkerApi;
