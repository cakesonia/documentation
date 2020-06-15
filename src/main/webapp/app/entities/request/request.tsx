import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { ICrudGetAllAction, TextFormat, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './request.reducer';
import { IRequest } from 'app/shared/model/request.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IRequestProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Request = (props: IRequestProps) => {
  const [paginationState, setPaginationState] = useState(getSortState(props.location, ITEMS_PER_PAGE));
  const [sorting, setSorting] = useState(false);

  const getAllEntities = () => {
    props.getEntities(paginationState.activePage - 1, paginationState.itemsPerPage, `${paginationState.sort},${paginationState.order}`);
  };

  const resetAll = () => {
    props.reset();
    setPaginationState({
      ...paginationState,
      activePage: 1
    });
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    props.reset();
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === 'asc' ? 'desc' : 'asc',
      sort: p
    });
    setSorting(true);
  };

  const { requestList, match, loading } = props;
  return (
    <div>
      <h2 id="request-heading">
        Requests
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp; Create new Request
        </Link>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          pageStart={paginationState.activePage}
          loadMore={handleLoadMore}
          hasMore={paginationState.activePage - 1 < props.links.next}
          loader={<div className="loader">Loading ...</div>}
          threshold={0}
          initialLoad={false}
        >
          {requestList && requestList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    ID <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('registrationDate')}>
                    Registration Date <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('deliveryDate')}>
                    Delivery Date <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('requestStatus')}>
                    Request Status <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Rent <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Car <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Client <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {requestList.map((request, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${request.id}`} color="link" size="sm">
                        {request.id}
                      </Button>
                    </td>
                    <td>
                      <TextFormat type="date" value={request.registrationDate} format={APP_DATE_FORMAT} />
                    </td>
                    <td>
                      <TextFormat type="date" value={request.deliveryDate} format={APP_DATE_FORMAT} />
                    </td>
                    <td>{request.requestStatus}</td>
                    <td>{request.rent ? <Link to={`rent/${request.rent.id}`}>{request.rent.id}</Link> : ''}</td>
                    <td>{request.car ? <Link to={`car/${request.car.id}`}>{request.car.id}</Link> : ''}</td>
                    <td>{request.client ? <Link to={`client/${request.client.id}`}>{request.client.id}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${request.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${request.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${request.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && <div className="alert alert-warning">No Requests found</div>
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

const mapStateToProps = ({ request }: IRootState) => ({
  requestList: request.entities,
  loading: request.loading,
  totalItems: request.totalItems,
  links: request.links,
  entity: request.entity,
  updateSuccess: request.updateSuccess
});

const mapDispatchToProps = {
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Request);
