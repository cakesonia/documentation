import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RentalPoint from './rental-point';
import RentalPointDetail from './rental-point-detail';
import RentalPointUpdate from './rental-point-update';
import RentalPointDeleteDialog from './rental-point-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={RentalPointDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RentalPointUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RentalPointUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RentalPointDetail} />
      <ErrorBoundaryRoute path={match.url} component={RentalPoint} />
    </Switch>
  </>
);

export default Routes;
