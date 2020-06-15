import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './car-brand.reducer';
import { ICarBrand } from 'app/shared/model/car-brand.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICarBrandDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CarBrandDetail = (props: ICarBrandDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { carBrandEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          CarBrand [<b>{carBrandEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{carBrandEntity.status}</dd>
        </dl>
        <Button tag={Link} to="/car-brand" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/car-brand/${carBrandEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ carBrand }: IRootState) => ({
  carBrandEntity: carBrand.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CarBrandDetail);
