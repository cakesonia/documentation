import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CarBrand from './car-brand';
import CarBrandDetail from './car-brand-detail';
import CarBrandUpdate from './car-brand-update';
import CarBrandDeleteDialog from './car-brand-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CarBrandDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CarBrandUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CarBrandUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CarBrandDetail} />
      <ErrorBoundaryRoute path={match.url} component={CarBrand} />
    </Switch>
  </>
);

export default Routes;
