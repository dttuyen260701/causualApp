import {baseApi} from "../../baseApi";

import {IGetAllJobInfoRes, IRegisterJobInput, IJobActionRes, IDeleteJobInfoInput, IGetAllJobInput} from "./types";

export const jobInfoApi = baseApi.injectEndpoints({
	endpoints: builder => ({
		getAllJobInfo: builder.query<IGetAllJobInfoRes, IGetAllJobInput>({
			query: (body) => `job-info/${body.workerId}/get-all-job-info?keyword=${body.searchText}`,
			providesTags: ['JobInfo']
		}),
		getJobInfoOfWorker: builder.query<IGetAllJobInfoRes, string>({
			query: (workerId) => `worker-info/${workerId}/list-job-info`,
			providesTags: ['JobInfoWorker']
		}),
		registerJobForWorker: builder.mutation<IJobActionRes, IRegisterJobInput>({
			query: body => {
				return {
					url: `job-info-of-worker/${body.userId}/register`,
					method: "post",
					headers: {
						"content-type": "application/json"
					},
					body: {jobInfoId: body.jobInfoId, note: body.note}
				}
			},
			invalidatesTags: ["JobRegister"]
		}),
		deleteJobInfoOfWorker: builder.mutation<IJobActionRes, IDeleteJobInfoInput>({
			query: body => {
				return {
					url: `job-info-of-worker/${body.workerId}/del/${body.jobInfoId}`,
					method: "get",
					headers: {
						"content-type": "application/json"
					}
				}
			},
			invalidatesTags: ["JobDelete"]
		})
	})
})

export const {useGetAllJobInfoQuery , useGetJobInfoOfWorkerQuery, useRegisterJobForWorkerMutation, useDeleteJobInfoOfWorkerMutation} = jobInfoApi;