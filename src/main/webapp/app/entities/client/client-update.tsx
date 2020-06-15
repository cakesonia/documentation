import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRentalPoint } from 'app/shared/model/rental-point.model';
import { getEntities as getRentalPoints } from 'app/entities/rental-point/rental-point.reducer';
import { getEntity, updateEntity, createEntity, reset } from './client.reducer';
import { IClient } from 'app/shared/model/client.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IClientUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ClientUpdate = (props: IClientUpdateProps) => {
  const [rentalPointsId, setRentalPointsId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { clientEntity, rentalPoints, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/client');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getRentalPoints();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...clientEntity,
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
          <h2 id="rentcarApp.client.home.createOrEditLabel">Create or edit a Client</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : clientEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="client-id">ID</Label>
                  <AvInput id="client-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="fullNameLabel" for="client-fullName">
                  Full Name
                </Label>
                <AvField
                  id="client-fullName"
                  type="text"
                  name="fullName"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="phoneLabel" for="client-phone">
                  Phone
                </Label>
                <AvField
                  id="client-phone"
                  type="text"
                  name="phone"
                  validate={{
                    pattern: { value: '[0-9]{10}', errorMessage: "This field should follow pattern for '[0-9]{10}'." }
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="addressLabel" for="client-address">
                  Address
                </Label>
                <AvField
                  id="client-address"
                  type="text"
                  name="address"
                  validate={{
                    maxLength: { value: 100, errorMessage: 'This field cannot be longer than 100 characters.' }
                  }}
                />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/client" replace color="info">
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
  rentalPoints: storeState.rentalPoint.entities,
  clientEntity: storeState.client.entity,
  loading: storeState.client.loading,
  updating: storeState.client.updating,
  updateSuccess: storeState.client.updateSuccess
});

const mapDispatchToProps = {
  getRentalPoints,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ClientUpdate);
