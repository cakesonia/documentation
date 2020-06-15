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
import { ICarType } from 'app/shared/model/car-type.model';
import { getEntities as getCarTypes } from 'app/entities/car-type/car-type.reducer';
import { ICarBrand } from 'app/shared/model/car-brand.model';
import { getEntities as getCarBrands } from 'app/entities/car-brand/car-brand.reducer';
import { IAutopark } from 'app/shared/model/autopark.model';
import { getEntities as getAutoparks } from 'app/entities/autopark/autopark.reducer';
import { getEntity, updateEntity, createEntity, reset } from './car.reducer';
import { ICar } from 'app/shared/model/car.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICarUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CarUpdate = (props: ICarUpdateProps) => {
  const [requestId, setRequestId] = useState('0');
  const [typeId, setTypeId] = useState('0');
  const [brandId, setBrandId] = useState('0');
  const [autoparkId, setAutoparkId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { carEntity, requests, carTypes, carBrands, autoparks, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/car');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getRequests();
    props.getCarTypes();
    props.getCarBrands();
    props.getAutoparks();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.manufacturedYear = convertDateTimeToServer(values.manufacturedYear);

    if (errors.length === 0) {
      const entity = {
        ...carEntity,
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
          <h2 id="rentcarApp.car.home.createOrEditLabel">Create or edit a Car</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : carEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="car-id">ID</Label>
                  <AvInput id="car-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="priceLabel" for="car-price">
                  Price
                </Label>
                <AvField id="car-price" type="string" className="form-control" name="price" />
              </AvGroup>
              <AvGroup>
                <Label id="manufacturedYearLabel" for="car-manufacturedYear">
                  Manufactured Year
                </Label>
                <AvInput
                  id="car-manufacturedYear"
                  type="datetime-local"
                  className="form-control"
                  name="manufacturedYear"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.carEntity.manufacturedYear)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="car-type">Type</Label>
                <AvInput id="car-type" type="select" className="form-control" name="type.id">
                  <option value="" key="0" />
                  {carTypes
                    ? carTypes.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="car-brand">Brand</Label>
                <AvInput id="car-brand" type="select" className="form-control" name="brand.id">
                  <option value="" key="0" />
                  {carBrands
                    ? carBrands.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="car-autopark">Autopark</Label>
                <AvInput id="car-autopark" type="select" className="form-control" name="autopark.id">
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
              <Button tag={Link} id="cancel-save" to="/car" replace color="info">
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
  carTypes: storeState.carType.entities,
  carBrands: storeState.carBrand.entities,
  autoparks: storeState.autopark.entities,
  carEntity: storeState.car.entity,
  loading: storeState.car.loading,
  updating: storeState.car.updating,
  updateSuccess: storeState.car.updateSuccess
});

const mapDispatchToProps = {
  getRequests,
  getCarTypes,
  getCarBrands,
  getAutoparks,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CarUpdate);
