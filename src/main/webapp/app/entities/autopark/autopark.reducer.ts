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

import { IAutopark, defaultValue } from 'app/shared/model/autopark.model';

export const ACTION_TYPES = {
  FETCH_AUTOPARK_LIST: 'autopark/FETCH_AUTOPARK_LIST',
  FETCH_AUTOPARK: 'autopark/FETCH_AUTOPARK',
  CREATE_AUTOPARK: 'autopark/CREATE_AUTOPARK',
  UPDATE_AUTOPARK: 'autopark/UPDATE_AUTOPARK',
  DELETE_AUTOPARK: 'autopark/DELETE_AUTOPARK',
  RESET: 'autopark/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAutopark>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type AutoparkState = Readonly<typeof initialState>;

// Reducer

export default (state: AutoparkState = initialState, action): AutoparkState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_AUTOPARK_LIST):
    case REQUEST(ACTION_TYPES.FETCH_AUTOPARK):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_AUTOPARK):
    case REQUEST(ACTION_TYPES.UPDATE_AUTOPARK):
    case REQUEST(ACTION_TYPES.DELETE_AUTOPARK):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_AUTOPARK_LIST):
    case FAILURE(ACTION_TYPES.FETCH_AUTOPARK):
    case FAILURE(ACTION_TYPES.CREATE_AUTOPARK):
    case FAILURE(ACTION_TYPES.UPDATE_AUTOPARK):
    case FAILURE(ACTION_TYPES.DELETE_AUTOPARK):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUTOPARK_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_AUTOPARK):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_AUTOPARK):
    case SUCCESS(ACTION_TYPES.UPDATE_AUTOPARK):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_AUTOPARK):
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

const apiUrl = 'api/autoparks';

// Actions

export const getEntities: ICrudGetAllAction<IAutopark> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_AUTOPARK_LIST,
    payload: axios.get<IAutopark>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IAutopark> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_AUTOPARK,
    payload: axios.get<IAutopark>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAutopark> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_AUTOPARK,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IAutopark> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_AUTOPARK,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAutopark> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_AUTOPARK,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
