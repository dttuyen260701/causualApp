import {IUserContactRegister, IUserLoginRegister, IUserPersonalRegister} from "../../app/api/register/registerType";
import {IUpdateUserInput} from "../../app/api/updateWorker/types";
import {IPersonalInfoValue} from "../../components/InfoManagement/types";

export interface IOrderStatus{
	status: string
	color: string
}

export const userLoginInfoEmpty : IUserLoginRegister = {
	UserName: "",
	Password: "",
	confirmPassword: "",
	Email: ""
}

export const userPersonalInfoEmpty: IUserPersonalRegister = {
	Name: "",
	IdentityCard: "",
	IdentityCardDate: "",
	IdentityCardBy: "",
	Gender: "",
	DateOfBirth: "",
	StartTime: "",
	EndTime: "",
}

export const userContactInfoEmpty: IUserContactRegister = {
	PhoneNumber: "",
	Address: "",
	AddressPoint: "",
	Province: "",
	District: "",
	Ward: "",
}

export const updateWorkerInfoEmpty : IUpdateUserInput = {
	name: "",
	id: "",
	gender: "",
	address: "",
	phone: "",
	identityCard: "",
	identityCardDate: "",
	identityCardBy: "",
	provinceId: "",
	provinceName: "",
	districtId: "",
	districtName: "",
	wardId: "",
	wardName: "",
	dateOfBirth:"",
	startWorkingTime:"",
	endWorkingTime: "",
	avatar: "",
	addressPoint: ""
}

export const PersonalInfoEmpty: IPersonalInfoValue = {
		id: '',
    	phone:  "",
    	name: '',
    	identityCard: '',
    	identityCardDate: '',
    	identityCardBy: '',
    	gender: 0,
    	dateOfBirth: '',
    	startWorkingTime: '',
    	endWorkingTime: '',
    	province: "",
		district: "",
		ward: "",
    	address: '',
    	avatar: '',
		addressPoint: ""
}

export const OrderStatus : IOrderStatus[] = [
	{status: "Đang thực hiện", color: '#FFCC33'},
	{status: "Đã huỷ đơn", color: 'red'},
	{status: "Đã hoàn thành", color: 'green'},
	{status: "Đang chờ", color: 'blue'},
	{status: "Đã từ chối", color: 'orange'},
	{status: "Đã chấp nhận", color: 'green'},
	{status: "Đang theo dõi", color: 'grey'},
]

export const GenderType : {id: number, type: string}[] = [
	{id: 0, type: "Nam"},
	{id: 1, type: "Nữ"},
	{id: 2, type: "Khác"},
];