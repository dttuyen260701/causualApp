export interface IUserLoginInput {
	userName: string;
	password: string;
	rememberMe: boolean;
}

export interface IUserInfo {
	userId: string;
	email: string;
	Name: string;
	profilePictureUrl: string;
	roleName: string;
}

export interface ILoginResponse {
	//token: string;
	//userInfo: IUserInfo;
	result: number;
	description: string;
}

export interface IErrorResponse {
	error: string;
	exceptionId: string;
	message: string;
	status: string;
}
