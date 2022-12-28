import {IUserInfo} from "../login/loginType"

export interface IUpdateUserInput {
	name: string,
	id: string,
	gender: string | number,
	address: string,
	phone: string,
	identityCard: string,
	identityCardDate: string,
	identityCardBy: string,
	provinceId: string,
	provinceName: string,
	districtId: string,
	districtName: string,
	wardId: string,
	wardName: string,
	dateOfBirth:string,
	startWorkingTime:string,
	endWorkingTime: string,
	avatar: string,
	addressPoint: string
}

export interface IWorkerInfoUpdateInput{
	workerId: string
	formdata: FormData
}

export interface IChangePassInput{
	newPassword: string,
	oldPassword: string,
	idUser: string
}

export interface IChangePassRes{
	isSuccessed: boolean,
	message: string,
	resultObj: string
}
export interface IUpdateUserRes{
	isSuccessed: boolean,
	message: string,
	resultObj: IUserInfo
}