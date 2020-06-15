import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRentalPoint, defaultValue } from 'app/shared/model/rental-point.model';

export const ACTION_TYPES = {
  FETCH_RENTALPOINT_LIST: 'rentalPoint/FETCH_RENTALPOINT_LIST',
  FETCH_RENTALPOINT: 'rentalPoint/FETCH_RENTALPOINT',
  CREATE_RENTALPOINT: 'rentalPoint/CREATE_RENTALPOINT',
  UPDATE_RENTALPOINT: 'rentalPoint/UPDATE_RENTALPOINT',
  DELETE_RENTALPOINT: 'rentalPoint/DELETE_RENTALPOINT',
  RESET: 'rentalPoint/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRentalPoint>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type RentalPointState = Readonly<typeof initialState>;

// Reducer

export default (state: RentalPointState = initialState, action): RentalPointState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_RENTALPOINT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RENTALPOINT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RENTALPOINT):
    case REQUEST(ACTION_TYPES.UPDATE_RENTALPOINT):
    case REQUEST(ACTION_TYPES.DELETE_RENTALPOINT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_RENTALPOINT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RENTALPOINT):
    case FAILURE(ACTION_TYPES.CREATE_RENTALPOINT):
    case FAILURE(ACTION_TYPES.UPDATE_RENTALPOINT):
    case FAILURE(ACTION_TYPES.DELETE_RENTALPOINT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_RENTALPOINT_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_RENTALPOINT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RENTALPOINT):
    case SUCCESS(ACTION_TYPES.UPDATE_RENTALPOINT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RENTALPOINT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/rental-points';

// Actions

export const getEntities: ICrudGetAllAction<IRentalPoint> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_RENTALPOINT_LIST,
    payload: axios.get<IRentalPoint>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IRentalPoint> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RENTALPOINT,
    payload: axios.get<IRentalPoint>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRentalPoint> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RENTALPOINT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IRentalPoint> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RENTALPOINT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRentalPoint> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RENTALPOINT,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
