import { IInterview } from 'app/shared/model/interview.model';

export interface IInterviewType {
  id?: number;
  type?: string;
  interview?: IInterview;
}

export class InterviewType implements IInterviewType {
  constructor(public id?: number, public type?: string, public interview?: IInterview) {}
}
