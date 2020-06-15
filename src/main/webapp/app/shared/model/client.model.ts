import { IRequest } from 'app/shared/model/request.model';
import { IRent } from 'app/shared/model/rent.model';
import { IFine } from 'app/shared/model/fine.model';
import { IRentalPoint } from 'app/shared/model/rental-point.model';

export interface IClient {
  id?: number;
  fullName?: string;
  phone?: string;
  address?: string;
  requests?: IRequest[];
  rents?: IRent[];
  fines?: IFine[];
  rentalPoints?: IRentalPoint[];
}

export const defaultValue: Readonly<IClient> = {};
