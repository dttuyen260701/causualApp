export interface IUserInputRegister {
	UserName: string;
	Password: string;
	Name: string;
	PhoneNumber: string;
	IdentityCard: string;
	IdentityCardDate: string;
	IdentityCardBy: string;
	Gender: string;
	DateOfBirth: string;
	Address: string;
	AddressPoint: string;
	StartTime: string;
	EndTime: string;
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
	Commune: string;
}
