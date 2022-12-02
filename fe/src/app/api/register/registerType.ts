import {IDistrict} from "../districts/districtTypes";
import {IProvince} from "../provinces/provinceType";
import {IWard} from "../wards/wardTypes";

export interface IUserInputRegister {
	userName: string;
	passWord: string;
	email: string;
	name: string;
	phone: string;
	identityCard: string;
	identityCardDate: string;
	identityCardBy: string;
	gender: string;
	dateOfBirth: string;
	address: string;
	addressPoint: string;
	startWorkingTime: string;
	endWorkingTime: string;
	provinceId: string;
	provinceName: string;
	districtId: string;
	districtName: string;
	wardId: string;
	wardName: string;
}

export interface IRegisterResponse {
	//token: string;
	//userInfo: IUserInfo;
	result: number;
	description: string;
}

export interface IUserLoginRegister {
	UserName: string;
	Password: string;
	confirmPassword: string;
	Email: string;
}

export interface IUserPersonalRegister {
	Name: string;
	IdentityCard: string;
	IdentityCardDate: string;
	IdentityCardBy: string;
	Gender: string;
	DateOfBirth: string;
	StartTime: string;
	EndTime: string;
}

export interface IUserContactRegister {
	PhoneNumber: string;
	Address: string;
	AddressPoint: string;
	Province: string;
	District: string;
	Ward: string;
}
