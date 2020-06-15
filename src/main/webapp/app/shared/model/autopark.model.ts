import { ICar } from 'app/shared/model/car.model';
import { IRentalPoint } from 'app/shared/model/rental-point.model';

export interface IAutopark {
  id?: number;
  location?: string;
  availableCars?: number;
  cars?: ICar[];
  rentalPoint?: IRentalPoint;
}

export const defaultValue: Readonly<IAutopark> = {};
