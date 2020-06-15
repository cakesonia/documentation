import { IInterview } from 'app/shared/model/interview.model';

export interface IInterviewResult {
  id?: number;
  description?: string;
  interview?: IInterview;
}

export class InterviewResult implements IInterviewResult {
  constructor(public id?: number, public description?: string, public interview?: IInterview) {}
}
