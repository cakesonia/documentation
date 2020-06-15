import { IInterview } from 'app/shared/model/interview.model';

export interface IInterviewer {
  id?: number;
  fullName?: string;
  email?: string;
  position?: string;
  interviews?: IInterview[];
}

export class Interviewer implements IInterviewer {
  constructor(
    public id?: number,
    public fullName?: string,
    public email?: string,
    public position?: string,
    public interviews?: IInterview[]
  ) {}
}
