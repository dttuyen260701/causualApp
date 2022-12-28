export interface IUserLoginInput {
	userName: string;
	password: string;
	rememberMe: boolean;
	withRole: number;
}

export interface IUserInfo {
	id: string
    email: string
    userName: string
    phone: number
    name: string
    identityCard: string
    identityCardDate: string
    identityCardBy: string
    gender: number
    dateOfBirth: string
    startWorkingTime: string
    endWorkingTime: string
    provinceId: string
    provinceName: string
    districtId: string
    districtName: string
    wardId: string
    wardName: string
    address: string
    addressPoint: string
    role: string
    avatar: string
    lastModificationTime: string
}

export interface ILoginResponse {
	//token: string;
	isSuccessed: boolean
	message: string
	resultObj: IUserInfo
}

export interface IErrorResponse {
	error: string;
	exceptionId: string;
	message: string;
	status: string;
}
