import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './car.reducer';
import { ICar } from 'app/shared/model/car.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICarDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CarDetail = (props: ICarDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { carEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Car [<b>{carEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="price">Price</span>
          </dt>
          <dd>{carEntity.price}</dd>
          <dt>
            <span id="manufacturedYear">Manufactured Year</span>
          </dt>
          <dd>
            <TextFormat value={carEntity.manufacturedYear} type="date" format={APP_DATE_FORMAT} />
          </dd>
          <dt>Type</dt>
          <dd>{carEntity.type ? carEntity.type.id : ''}</dd>
          <dt>Brand</dt>
          <dd>{carEntity.brand ? carEntity.brand.id : ''}</dd>
          <dt>Autopark</dt>
          <dd>{carEntity.autopark ? carEntity.autopark.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/car" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/car/${carEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ car }: IRootState) => ({
  carEntity: car.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CarDetail);
