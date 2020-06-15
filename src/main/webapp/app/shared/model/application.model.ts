import { Moment } from 'moment';
import { IApplicationStatus } from 'app/shared/model/application-status.model';
import { IVacancy } from 'app/shared/model/vacancy.model';

export interface IApplication {
  id?: number;
  applicationDate?: Moment;
  statuses?: IApplicationStatus[];
  vacancy?: IVacancy;
}

export class Application implements IApplication {
  constructor(public id?: number, public applicationDate?: Moment, public statuses?: IApplicationStatus[], public vacancy?: IVacancy) {}
}
