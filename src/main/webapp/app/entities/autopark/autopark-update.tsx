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
import { getEntity, updateEntity, createEntity, reset } from './autopark.reducer';
import { IAutopark } from 'app/shared/model/autopark.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAutoparkUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AutoparkUpdate = (props: IAutoparkUpdateProps) => {
  const [rentalPointId, setRentalPointId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { autoparkEntity, rentalPoints, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/autopark');
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
        ...autoparkEntity,
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
          <h2 id="rentcarApp.autopark.home.createOrEditLabel">Create or edit a Autopark</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : autoparkEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="autopark-id">ID</Label>
                  <AvInput id="autopark-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="locationLabel" for="autopark-location">
                  Location
                </Label>
                <AvField id="autopark-location" type="text" name="location" />
              </AvGroup>
              <AvGroup>
                <Label id="availableCarsLabel" for="autopark-availableCars">
                  Available Cars
                </Label>
                <AvField id="autopark-availableCars" type="string" className="form-control" name="availableCars" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/autopark" replace color="info">
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
  autoparkEntity: storeState.autopark.entity,
  loading: storeState.autopark.loading,
  updating: storeState.autopark.updating,
  updateSuccess: storeState.autopark.updateSuccess
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

export default connect(mapStateToProps, mapDispatchToProps)(AutoparkUpdate);
