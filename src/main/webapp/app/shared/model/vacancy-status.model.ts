import { IVacancy } from 'app/shared/model/vacancy.model';

export interface IVacancyStatus {
  id?: number;
  status?: string;
  vacancy?: IVacancy;
}

export class VacancyStatus implements IVacancyStatus {
  constructor(public id?: number, public status?: string, public vacancy?: IVacancy) {}
}
