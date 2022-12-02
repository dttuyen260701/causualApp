export interface IGetAllJobResponse{
isSuccessed: boolean,
message: string
resultObj: ITypeOfJob[]
}

export interface ITypeOfJob{
	id: string
	name: string
	image: string
	des: string
}