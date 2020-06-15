import { IApplication } from 'app/shared/model/application.model';

export interface IApplicationStatus {
  id?: number;
  status?: string;
  application?: IApplication;
}

export class ApplicationStatus implements IApplicationStatus {
  constructor(public id?: number, public status?: string, public application?: IApplication) {}
}
