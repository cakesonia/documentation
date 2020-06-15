import { ICar } from 'app/shared/model/car.model';

export interface ICarBrand {
  id?: number;
  status?: string;
  cars?: ICar[];
}

export const defaultValue: Readonly<ICarBrand> = {};
