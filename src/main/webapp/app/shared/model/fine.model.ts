import { IClient } from 'app/shared/model/client.model';
import { IRent } from 'app/shared/model/rent.model';

export interface IFine {
  id?: number;
  fineSize?: number;
  fineReason?: string;
  client?: IClient;
  rent?: IRent;
}

export const defaultValue: Readonly<IFine> = {};
