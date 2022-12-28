import {IOption} from "../../common/shared-components/Select/SelectTypes"

export interface IAccountInfo{
	newPassword: string,
	oldPassword: string,
	userName: string,
	email: string
}

export interface IPersonalInfoValue{
	name: string,
	id: string,
	gender: string | number,
	address: string,
	phone: string,
	identityCard: string,
	identityCardDate: string,
	identityCardBy: string,
	province: string,
	district: string,
	ward: string
	dateOfBirth:string,
	startWorkingTime:string,
	endWorkingTime: string,
	avatar: string
	addressPoint: string
}