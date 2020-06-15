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

import { IFine, defaultValue } from 'app/shared/model/fine.model';

export const ACTION_TYPES = {
  FETCH_FINE_LIST: 'fine/FETCH_FINE_LIST',
  FETCH_FINE: 'fine/FETCH_FINE',
  CREATE_FINE: 'fine/CREATE_FINE',
  UPDATE_FINE: 'fine/UPDATE_FINE',
  DELETE_FINE: 'fine/DELETE_FINE',
  RESET: 'fine/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFine>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type FineState = Readonly<typeof initialState>;

// Reducer

export default (state: FineState = initialState, action): FineState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FINE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FINE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_FINE):
    case REQUEST(ACTION_TYPES.UPDATE_FINE):
    case REQUEST(ACTION_TYPES.DELETE_FINE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_FINE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FINE):
    case FAILURE(ACTION_TYPES.CREATE_FINE):
    case FAILURE(ACTION_TYPES.UPDATE_FINE):
    case FAILURE(ACTION_TYPES.DELETE_FINE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_FINE_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_FINE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_FINE):
    case SUCCESS(ACTION_TYPES.UPDATE_FINE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_FINE):
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

const apiUrl = 'api/fines';

// Actions

export const getEntities: ICrudGetAllAction<IFine> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FINE_LIST,
    payload: axios.get<IFine>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IFine> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FINE,
    payload: axios.get<IFine>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IFine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FINE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IFine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FINE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFine> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FINE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
