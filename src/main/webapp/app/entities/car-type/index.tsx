import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CarType from './car-type';
import CarTypeDetail from './car-type-detail';
import CarTypeUpdate from './car-type-update';
import CarTypeDeleteDialog from './car-type-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={CarTypeDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CarTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CarTypeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CarTypeDetail} />
      <ErrorBoundaryRoute path={match.url} component={CarType} />
    </Switch>
  </>
);

export default Routes;
