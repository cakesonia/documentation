import { Moment } from 'moment';
import { IAutopark } from 'app/shared/model/autopark.model';
import { IClient } from 'app/shared/model/client.model';

export interface IRentalPoint {
  id?: number;
  location?: string;
  worktime?: Moment;
  autopark?: IAutopark;
  clients?: IClient[];
}

export const defaultValue: Readonly<IRentalPoint> = {};
