import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import client, {
  ClientState
} from 'app/entities/client/client.reducer';
// prettier-ignore
import rent, {
  RentState
} from 'app/entities/rent/rent.reducer';
// prettier-ignore
import request, {
  RequestState
} from 'app/entities/request/request.reducer';
// prettier-ignore
import fine, {
  FineState
} from 'app/entities/fine/fine.reducer';
// prettier-ignore
import carType, {
  CarTypeState
} from 'app/entities/car-type/car-type.reducer';
// prettier-ignore
import car, {
  CarState
} from 'app/entities/car/car.reducer';
// prettier-ignore
import carBrand, {
  CarBrandState
} from 'app/entities/car-brand/car-brand.reducer';
// prettier-ignore
import autopark, {
  AutoparkState
} from 'app/entities/autopark/autopark.reducer';
// prettier-ignore
import rentalPoint, {
  RentalPointState
} from 'app/entities/rental-point/rental-point.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly client: ClientState;
  readonly rent: RentState;
  readonly request: RequestState;
  readonly fine: FineState;
  readonly carType: CarTypeState;
  readonly car: CarState;
  readonly carBrand: CarBrandState;
  readonly autopark: AutoparkState;
  readonly rentalPoint: RentalPointState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  client,
  rent,
  request,
  fine,
  carType,
  car,
  carBrand,
  autopark,
  rentalPoint,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
