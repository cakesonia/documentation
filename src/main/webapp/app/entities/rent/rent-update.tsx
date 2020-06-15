import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRequest } from 'app/shared/model/request.model';
import { getEntities as getRequests } from 'app/entities/request/request.reducer';
import { IClient } from 'app/shared/model/client.model';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { getEntity, updateEntity, createEntity, reset } from './rent.reducer';
import { IRent } from 'app/shared/model/rent.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RentUpdate = (props: IRentUpdateProps) => {
  const [requestId, setRequestId] = useState('0');
  const [clientId, setClientId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { rentEntity, requests, clients, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/rent');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getRequests();
    props.getClients();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.lendingDate = convertDateTimeToServer(values.lendingDate);
    values.returningDate = convertDateTimeToServer(values.returningDate);

    if (errors.length === 0) {
      const entity = {
        ...rentEntity,
        ...values
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rentcarApp.rent.home.createOrEditLabel">Create or edit a Rent</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : rentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="rent-id">ID</Label>
                  <AvInput id="rent-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="lendingDateLabel" for="rent-lendingDate">
                  Lending Date
                </Label>
                <AvInput
                  id="rent-lendingDate"
                  type="datetime-local"
                  className="form-control"
                  name="lendingDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.rentEntity.lendingDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="returningDateLabel" for="rent-returningDate">
                  Returning Date
                </Label>
                <AvInput
                  id="rent-returningDate"
                  type="datetime-local"
                  className="form-control"
                  name="returningDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.rentEntity.returningDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="rentStatusLabel" for="rent-rentStatus">
                  Rent Status
                </Label>
                <AvField id="rent-rentStatus" type="text" name="rentStatus" />
              </AvGroup>
              <AvGroup>
                <Label for="rent-client">Client</Label>
                <AvInput id="rent-client" type="select" className="form-control" name="client.id">
                  <option value="" key="0" />
                  {clients
                    ? clients.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/rent" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  requests: storeState.request.entities,
  clients: storeState.client.entities,
  rentEntity: storeState.rent.entity,
  loading: storeState.rent.loading,
  updating: storeState.rent.updating,
  updateSuccess: storeState.rent.updateSuccess
});

const mapDispatchToProps = {
  getRequests,
  getClients,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RentUpdate);
