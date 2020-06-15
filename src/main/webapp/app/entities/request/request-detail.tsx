import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './request.reducer';
import { IRequest } from 'app/shared/model/request.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRequestDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RequestDetail = (props: IRequestDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { requestEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Request [<b>{requestEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="registrationDate">Registration Date</span>
          </dt>
          <dd>
            <TextFormat value={requestEntity.registrationDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="deliveryDate">Delivery Date</span>
          </dt>
          <dd>
            <TextFormat value={requestEntity.deliveryDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="requestStatus">Request Status</span>
          </dt>
          <dd>{requestEntity.requestStatus}</dd>
          <dt>Rent</dt>
          <dd>{requestEntity.rent ? requestEntity.rent.id : ''}</dd>
          <dt>Car</dt>
          <dd>{requestEntity.car ? requestEntity.car.id : ''}</dd>
          <dt>Client</dt>
          <dd>{requestEntity.client ? requestEntity.client.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/request" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/request/${requestEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ request }: IRootState) => ({
  requestEntity: request.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RequestDetail);
