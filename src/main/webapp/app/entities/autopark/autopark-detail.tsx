import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './autopark.reducer';
import { IAutopark } from 'app/shared/model/autopark.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAutoparkDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const AutoparkDetail = (props: IAutoparkDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { autoparkEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Autopark [<b>{autoparkEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="location">Location</span>
          </dt>
          <dd>{autoparkEntity.location}</dd>
          <dt>
            <span id="availableCars">Available Cars</span>
          </dt>
          <dd>{autoparkEntity.availableCars}</dd>
        </dl>
        <Button tag={Link} to="/autopark" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/autopark/${autoparkEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ autopark }: IRootState) => ({
  autoparkEntity: autopark.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(AutoparkDetail);
