
export interface IGetAllJobInfoRes{
	isSuccessed: boolean
	message: string
	resultObj: IJobInfo[]
}

export interface IGetAllJobInput{
	workerId: string
	searchText: string
}

export interface IGetJobStaticticRes{
	isSuccessed: boolean
	message: string
	resultObj: IJobStatictic[]
}

export interface IJobActionRes{
	isSuccessed: boolean
	message: string
	resultObj: string
}

export interface IJobTotalInfoRes{
	isSuccessed: boolean
	message: string
	resultObj: IJobTotal
}

export interface IJobTotal{
	totalJob: number
	totalOrder: number
	totalRevenue: number
}

export interface IRegisterJobInput{
	userId: string
	jobInfoId: string
	note: string
}
export interface IDeleteJobInfoInput{
	workerId: string
	jobInfoId: string 
}
export interface IJobStatictic{
	month: string
	totalOrder: string
	revenue: string
}

export interface IJobInfo{
	id: string,
	name: string,
	price: number,
	description: string,
	typeOfJobId: string,
	typeOfJobName: string,
	image: string,
	note: string,
	creationTime: string
}

export interface IOrder{
	id: string
	jobId: string
	customerId: string
	workerId: string
	note: string
	jobPrices: string
	creationTime: string
	userAddress: string
	userPoint: string
	isPaid: boolean
	status: number
	jobInfoName: string
	jobInfoImage: string
	customerName: string
	customerImage: string
	customerPhone: string
	workerName: string
	workerImage: string
	workerPhone: string
}

export interface IGetListOrderInput{
	workerId: string
	pageIndex: number
	pageSize: number
	keyword: string
}

export interface IGetListOrderRes{
	isSuccessed: boolean
	message: string
	resultObj: {
		items: IOrder[]
		pageIndex: number
        pageSize: number
        totalRecords: number
        pageCount: number
	}
}

export interface IRateWorker{
	attitudeRateAverage: number
    skillRateAverage: number
   	pleasureRateAverage: number
	rateAverage: number
}

export interface IRateWorkerRes{
	isSuccessed: boolean
	message: string
	resultObj: IRateWorker
}