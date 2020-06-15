import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IClient } from 'app/shared/model/client.model';
import { getEntities as getClients } from 'app/entities/client/client.reducer';
import { IRent } from 'app/shared/model/rent.model';
import { getEntities as getRents } from 'app/entities/rent/rent.reducer';
import { getEntity, updateEntity, createEntity, reset } from './fine.reducer';
import { IFine } from 'app/shared/model/fine.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFineUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FineUpdate = (props: IFineUpdateProps) => {
  const [clientId, setClientId] = useState('0');
  const [rentId, setRentId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { fineEntity, clients, rents, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/fine');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getClients();
    props.getRents();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...fineEntity,
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
          <h2 id="rentcarApp.fine.home.createOrEditLabel">Create or edit a Fine</h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : fineEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="fine-id">ID</Label>
                  <AvInput id="fine-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="fineSizeLabel" for="fine-fineSize">
                  Fine Size
                </Label>
                <AvField id="fine-fineSize" type="string" className="form-control" name="fineSize" />
              </AvGroup>
              <AvGroup>
                <Label id="fineReasonLabel" for="fine-fineReason">
                  Fine Reason
                </Label>
                <AvField id="fine-fineReason" type="text" name="fineReason" />
              </AvGroup>
              <AvGroup>
                <Label for="fine-client">Client</Label>
                <AvInput id="fine-client" type="select" className="form-control" name="client.id">
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
              <AvGroup>
                <Label for="fine-rent">Rent</Label>
                <AvInput id="fine-rent" type="select" className="form-control" name="rent.id">
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
              <Button tag={Link} id="cancel-save" to="/fine" replace color="info">
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
  clients: storeState.client.entities,
  rents: storeState.rent.entities,
  fineEntity: storeState.fine.entity,
  loading: storeState.fine.loading,
  updating: storeState.fine.updating,
  updateSuccess: storeState.fine.updateSuccess
});

const mapDispatchToProps = {
  getClients,
  getRents,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FineUpdate);
