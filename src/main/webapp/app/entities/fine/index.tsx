import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Fine from './fine';
import FineDetail from './fine-detail';
import FineUpdate from './fine-update';
import FineDeleteDialog from './fine-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={FineDeleteDialog} />
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FineDetail} />
      <ErrorBoundaryRoute path={match.url} component={Fine} />
    </Switch>
  </>
);

export default Routes;
