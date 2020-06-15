import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './rent.reducer';
import { IRent } from 'app/shared/model/rent.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RentDetail = (props: IRentDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { rentEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Rent [<b>{rentEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="lendingDate">Lending Date</span>
          </dt>
          <dd>
            <TextFormat value={rentEntity.lendingDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="returningDate">Returning Date</span>
          </dt>
          <dd>
            <TextFormat value={rentEntity.returningDate} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>
            <span id="rentStatus">Rent Status</span>
          </dt>
          <dd>{rentEntity.rentStatus}</dd>
          <dt>Client</dt>
          <dd>{rentEntity.client ? rentEntity.client.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/rent" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rent/${rentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ rent }: IRootState) => ({
  rentEntity: rent.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RentDetail);
