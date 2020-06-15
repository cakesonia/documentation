import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Client from './client';
import Rent from './rent';
import Request from './request';
import Fine from './fine';
import CarType from './car-type';
import Car from './car';
import CarBrand from './car-brand';
import Autopark from './autopark';
import RentalPoint from './rental-point';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}client`} component={Client} />
      <ErrorBoundaryRoute path={`${match.url}rent`} component={Rent} />
      <ErrorBoundaryRoute path={`${match.url}request`} component={Request} />
      <ErrorBoundaryRoute path={`${match.url}fine`} component={Fine} />
      <ErrorBoundaryRoute path={`${match.url}car-type`} component={CarType} />
      <ErrorBoundaryRoute path={`${match.url}car`} component={Car} />
      <ErrorBoundaryRoute path={`${match.url}car-brand`} component={CarBrand} />
      <ErrorBoundaryRoute path={`${match.url}autopark`} component={Autopark} />
      <ErrorBoundaryRoute path={`${match.url}rental-point`} component={RentalPoint} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
