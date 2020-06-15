import { Moment } from 'moment';
import { IVacancyStatus } from 'app/shared/model/vacancy-status.model';
import { IApplication } from 'app/shared/model/application.model';
import { ICandidate } from 'app/shared/model/candidate.model';

export interface IVacancy {
  id?: number;
  title?: string;
  description?: string;
  salary?: number;
  createdDate?: Moment;
  statuses?: IVacancyStatus[];
  applications?: IApplication[];
  candidates?: ICandidate[];
}

export class Vacancy implements IVacancy {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string,
    public salary?: number,
    public createdDate?: Moment,
    public statuses?: IVacancyStatus[],
    public applications?: IApplication[],
    public candidates?: ICandidate[]
  ) {}
}
