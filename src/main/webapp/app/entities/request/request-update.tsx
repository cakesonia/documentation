import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRent } from 'app/shared/model/rent.model';
import { getEntities as getRents } from 'app/entities/rent/rent.reducer';
import { ICar } from 'app/shared/model/car.model';
import { getEntities as getCars } from 'app/entities/car/car.reducer';
import { IClient } from 'app/shared/model/client.model';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { getEntity, updateEntity, createEntity, reset } from './request.reducer';
import { IRequest } from 'app/shared/model/request.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRequestUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RequestUpdate = (props: IRequestUpdateProps) => {
  const [rentId, setRentId] = useState('0');
  const [carId, setCarId] = useState('0');
  const [clientId, setClientId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { requestEntity, rents, cars, clients, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/request');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getRents();
    props.getCars();
    props.getClients();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.registrationDate = convertDateTimeToServer(values.registrationDate);
    values.deliveryDate = convertDateTimeToServer(values.deliveryDate);

    if (errors.length === 0) {
      const entity = {
        ...requestEntity,
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
          <h2 id="rentcarApp.request.home.createOrEditLabel">Create or edit a Request</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : requestEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="request-id">ID</Label>
                  <AvInput id="request-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="registrationDateLabel" for="request-registrationDate">
                  Registration Date
                </Label>
                <AvInput
                  id="request-registrationDate"
                  type="datetime-local"
                  className="form-control"
                  name="registrationDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.requestEntity.registrationDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="deliveryDateLabel" for="request-deliveryDate">
                  Delivery Date
                </Label>
                <AvInput
                  id="request-deliveryDate"
                  type="datetime-local"
                  className="form-control"
                  name="deliveryDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.requestEntity.deliveryDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="requestStatusLabel" for="request-requestStatus">
                  Request Status
                </Label>
                <AvField id="request-requestStatus" type="text" name="requestStatus" />
              </AvGroup>
              <AvGroup>
                <Label for="request-rent">Rent</Label>
                <AvInput id="request-rent" type="select" className="form-control" name="rent.id">
                  <option value="" key="0" />
                  {rents
                    ? rents.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="request-car">Car</Label>
                <AvInput id="request-car" type="select" className="form-control" name="car.id">
                  <option value="" key="0" />
                  {cars
                    ? cars.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="request-client">Client</Label>
                <AvInput id="request-client" type="select" className="form-control" name="client.id">
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
              <Button tag={Link} id="cancel-save" to="/request" replace color="info">
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
  rents: storeState.rent.entities,
  cars: storeState.car.entities,
  clients: storeState.client.entities,
  requestEntity: storeState.request.entity,
  loading: storeState.request.loading,
  updating: storeState.request.updating,
  updateSuccess: storeState.request.updateSuccess
});

const mapDispatchToProps = {
  getRents,
  getCars,
  getClients,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RequestUpdate);
