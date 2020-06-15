import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IAutopark } from 'app/shared/model/autopark.model';
import { getEntities as getAutoparks } from 'app/entities/autopark/autopark.reducer';
import { IClient } from 'app/shared/model/client.model';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { getEntity, updateEntity, createEntity, reset } from './rental-point.reducer';
import { IRentalPoint } from 'app/shared/model/rental-point.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRentalPointUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RentalPointUpdate = (props: IRentalPointUpdateProps) => {
  const [idsclients, setIdsclients] = useState([]);
  const [autoparkId, setAutoparkId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { rentalPointEntity, autoparks, clients, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/rental-point');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getAutoparks();
    props.getClients();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.worktime = convertDateTimeToServer(values.worktime);

    if (errors.length === 0) {
      const entity = {
        ...rentalPointEntity,
        ...values,
        clients: mapIdList(values.clients)
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
          <h2 id="rentcarApp.rentalPoint.home.createOrEditLabel">Create or edit a RentalPoint</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : rentalPointEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="rental-point-id">ID</Label>
                  <AvInput id="rental-point-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="locationLabel" for="rental-point-location">
                  Location
                </Label>
                <AvField id="rental-point-location" type="text" name="location" />
              </AvGroup>
              <AvGroup>
                <Label id="worktimeLabel" for="rental-point-worktime">
                  Worktime
                </Label>
                <AvInput
                  id="rental-point-worktime"
                  type="datetime-local"
                  className="form-control"
                  name="worktime"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.rentalPointEntity.worktime)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="rental-point-autopark">Autopark</Label>
                <AvInput id="rental-point-autopark" type="select" className="form-control" name="autopark.id">
                  <option value="" key="0" />
                  {autoparks
                    ? autoparks.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="rental-point-clients">Clients</Label>
                <AvInput
                  id="rental-point-clients"
                  type="select"
                  multiple
                  className="form-control"
                  name="clients"
                  value={rentalPointEntity.clients && rentalPointEntity.clients.map(e => e.id)}
                >
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
              <Button tag={Link} id="cancel-save" to="/rental-point" replace color="info">
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
  autoparks: storeState.autopark.entities,
  clients: storeState.client.entities,
  rentalPointEntity: storeState.rentalPoint.entity,
  loading: storeState.rentalPoint.loading,
  updating: storeState.rentalPoint.updating,
  updateSuccess: storeState.rentalPoint.updateSuccess
});

const mapDispatchToProps = {
  getAutoparks,
  getClients,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RentalPointUpdate);
