import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Rent from './rent';
import RentDetail from './rent-detail';
import RentUpdate from './rent-update';
import RentDeleteDialog from './rent-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RentDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Rent} />
    </Switch>
  </>
);

export default Routes;
