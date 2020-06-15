import { IInterview } from 'app/shared/model/interview.model';
import { IVacancy } from 'app/shared/model/vacancy.model';

export interface ICandidate {
  id?: number;
  fullName?: string;
  email?: string;
  cvUrl?: string;
  interviews?: IInterview[];
  vacancies?: IVacancy[];
}

export class Candidate implements ICandidate {
  constructor(
    public id?: number,
    public fullName?: string,
    public email?: string,
    public cvUrl?: string,
    public interviews?: IInterview[],
    public vacancies?: IVacancy[]
  ) {}
}
