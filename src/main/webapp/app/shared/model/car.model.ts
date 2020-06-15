import { Moment } from 'moment';
import { IRequest } from 'app/shared/model/request.model';
import { ICarType } from 'app/shared/model/car-type.model';
import { ICarBrand } from 'app/shared/model/car-brand.model';
import { IAutopark } from 'app/shared/model/autopark.model';

export interface ICar {
  id?: number;
  price?: number;
  manufacturedYear?: Moment;
  request?: IRequest;
  type?: ICarType;
  brand?: ICarBrand;
  autopark?: IAutopark;
}

export const defaultValue: Readonly<ICar> = {};
