import {IDeleteJobInfoInput, IJobInfo} from "../../../app/api/jobManageWorker/types";

export interface IJobInfoCard{
	jobInfo: IJobInfo
	handleClickRegister: (jobId: string) => void
	handleDeleteJobInfo: (jobInfo: IDeleteJobInfoInput) => void
}