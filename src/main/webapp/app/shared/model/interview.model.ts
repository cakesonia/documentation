import { Moment } from 'moment';
import { IInterviewResult } from 'app/shared/model/interview-result.model';
import { IInterviewType } from 'app/shared/model/interview-type.model';
import { ICandidate } from 'app/shared/model/candidate.model';
import { IInterviewer } from 'app/shared/model/interviewer.model';

export interface IInterview {
  id?: number;
  date?: Moment;
  result?: IInterviewResult;
  types?: IInterviewType[];
  candidate?: ICandidate;
  interviewer?: IInterviewer;
}

export class Interview implements IInterview {
  constructor(
    public id?: number,
    public date?: Moment,
    public result?: IInterviewResult,
    public types?: IInterviewType[],
    public candidate?: ICandidate,
    public interviewer?: IInterviewer
  ) {}
}
