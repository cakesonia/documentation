import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICarBrand, defaultValue } from 'app/shared/model/car-brand.model';

export const ACTION_TYPES = {
  FETCH_CARBRAND_LIST: 'carBrand/FETCH_CARBRAND_LIST',
  FETCH_CARBRAND: 'carBrand/FETCH_CARBRAND',
  CREATE_CARBRAND: 'carBrand/CREATE_CARBRAND',
  UPDATE_CARBRAND: 'carBrand/UPDATE_CARBRAND',
  DELETE_CARBRAND: 'carBrand/DELETE_CARBRAND',
  RESET: 'carBrand/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICarBrand>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CarBrandState = Readonly<typeof initialState>;

// Reducer

export default (state: CarBrandState = initialState, action): CarBrandState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CARBRAND_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CARBRAND):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CARBRAND):
    case REQUEST(ACTION_TYPES.UPDATE_CARBRAND):
    case REQUEST(ACTION_TYPES.DELETE_CARBRAND):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CARBRAND_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CARBRAND):
    case FAILURE(ACTION_TYPES.CREATE_CARBRAND):
    case FAILURE(ACTION_TYPES.UPDATE_CARBRAND):
    case FAILURE(ACTION_TYPES.DELETE_CARBRAND):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CARBRAND_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CARBRAND):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CARBRAND):
    case SUCCESS(ACTION_TYPES.UPDATE_CARBRAND):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CARBRAND):
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

const apiUrl = 'api/car-brands';

// Actions

export const getEntities: ICrudGetAllAction<ICarBrand> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CARBRAND_LIST,
  payload: axios.get<ICarBrand>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICarBrand> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CARBRAND,
    payload: axios.get<ICarBrand>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICarBrand> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CARBRAND,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICarBrand> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CARBRAND,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICarBrand> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CARBRAND,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
