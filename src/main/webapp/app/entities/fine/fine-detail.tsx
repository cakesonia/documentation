import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './fine.reducer';
import { IFine } from 'app/shared/model/fine.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFineDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const FineDetail = (props: IFineDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { fineEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          Fine [<b>{fineEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="fineSize">Fine Size</span>
          </dt>
          <dd>{fineEntity.fineSize}</dd>
          <dt>
            <span id="fineReason">Fine Reason</span>
          </dt>
          <dd>{fineEntity.fineReason}</dd>
          <dt>Client</dt>
          <dd>{fineEntity.client ? fineEntity.client.id : ''}</dd>
          <dt>Rent</dt>
          <dd>{fineEntity.rent ? fineEntity.rent.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/fine" replace color="info">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/fine/${fineEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ fine }: IRootState) => ({
  fineEntity: fine.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(FineDetail);
