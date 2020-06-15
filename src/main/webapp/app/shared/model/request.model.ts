import { Moment } from 'moment';
import { IRent } from 'app/shared/model/rent.model';
import { ICar } from 'app/shared/model/car.model';
import { IClient } from 'app/shared/model/client.model';

export interface IRequest {
  id?: number;
  registrationDate?: Moment;
  deliveryDate?: Moment;
  requestStatus?: string;
  rent?: IRent;
  car?: ICar;
  client?: IClient;
}

export const defaultValue: Readonly<IRequest> = {};
