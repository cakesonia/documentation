import { ICar } from 'app/shared/model/car.model';

export interface ICarType {
  id?: number;
  status?: string;
  cars?: ICar[];
}

export const defaultValue: Readonly<ICarType> = {};
