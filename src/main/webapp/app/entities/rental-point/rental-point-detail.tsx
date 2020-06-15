import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './rental-point.reducer';
import { IRentalPoint } from 'app/shared/model/rental-point.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRentalPointDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const RentalPointDetail = (props: IRentalPointDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { rentalPointEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          RentalPoint [<b>{rentalPointEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="location">Location</span>
          </dt>
          <dd>{rentalPointEntity.location}</dd>
          <dt>
            <span id="worktime">Worktime</span>
          </dt>
          <dd>
            <TextFormat value={rentalPointEntity.worktime} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>Autopark</dt>
          <dd>{rentalPointEntity.autopark ? rentalPointEntity.autopark.id : ''}</dd>
          <dt>Clients</dt>
          <dd>
            {rentalPointEntity.clients
              ? rentalPointEntity.clients.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {i === rentalPointEntity.clients.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/rental-point" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/rental-point/${rentalPointEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ rentalPoint }: IRootState) => ({
  rentalPointEntity: rentalPoint.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(RentalPointDetail);
