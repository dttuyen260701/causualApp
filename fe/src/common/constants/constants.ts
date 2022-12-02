import {IUserContactRegister, IUserLoginRegister, IUserPersonalRegister} from "../../app/api/register/registerType";

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
