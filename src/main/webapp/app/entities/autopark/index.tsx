import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Autopark from './autopark';
import AutoparkDetail from './autopark-detail';
import AutoparkUpdate from './autopark-update';
import AutoparkDeleteDialog from './autopark-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AutoparkDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AutoparkUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AutoparkUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AutoparkDetail} />
      <ErrorBoundaryRoute path={match.url} component={Autopark} />
    </Switch>
  </>
);

export default Routes;
