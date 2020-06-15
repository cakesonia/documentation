import { Moment } from 'moment';
import { IFine } from 'app/shared/model/fine.model';
import { IRequest } from 'app/shared/model/request.model';
import { IClient } from 'app/shared/model/client.model';

export interface IRent {
  id?: number;
  lendingDate?: Moment;
  returningDate?: Moment;
  rentStatus?: string;
  fines?: IFine[];
  request?: IRequest;
  client?: IClient;
}

export const defaultValue: Readonly<IRent> = {};
