import {baseApi} from "../../baseApi";

import {IGetJobStaticticRes, IGetListOrderInput, IGetListOrderRes, IJobTotalInfoRes, IRateWorkerRes} from "./types";

export const jobStatisticApi = baseApi.injectEndpoints({
	endpoints: builder => ({
		get6MonthJobStatictic: builder.query<IGetJobStaticticRes, string>({
			query: (workerId) => `order/${workerId}/history-order-six-month`,
			providesTags: ['JobStatistic']
		}),
		getListOrderOfWorker: builder.query<IGetListOrderRes, IGetListOrderInput>({
			query: (body) => `order/${body.workerId}/list-order?PageIndex=${body.pageIndex}&PageSize=${body.pageSize}&Keyword=${body.keyword}`,
			providesTags: ['OrderList']
		}),
		getJobTotalInfoOfWorker: builder.query<IJobTotalInfoRes, string>({
			query: (workerId) => `worker-info/${workerId}/totalRevenue-totalJob-totalOrder`,
			providesTags: ['JobTotal']
		}),
		getRateOfWorker: builder.query<IRateWorkerRes, string>({
			query: (workerId) => `rate-of-worker/${workerId}/detail`,
			providesTags: ['RateWorker']
		})
	})
});

export const {useGet6MonthJobStaticticQuery, useGetListOrderOfWorkerQuery, useGetJobTotalInfoOfWorkerQuery, useGetRateOfWorkerQuery} = jobStatisticApi;