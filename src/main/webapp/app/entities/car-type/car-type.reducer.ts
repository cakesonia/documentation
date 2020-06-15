import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICarType, defaultValue } from 'app/shared/model/car-type.model';

export const ACTION_TYPES = {
  FETCH_CARTYPE_LIST: 'carType/FETCH_CARTYPE_LIST',
  FETCH_CARTYPE: 'carType/FETCH_CARTYPE',
  CREATE_CARTYPE: 'carType/CREATE_CARTYPE',
  UPDATE_CARTYPE: 'carType/UPDATE_CARTYPE',
  DELETE_CARTYPE: 'carType/DELETE_CARTYPE',
  RESET: 'carType/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICarType>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type CarTypeState = Readonly<typeof initialState>;

// Reducer

export default (state: CarTypeState = initialState, action): CarTypeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CARTYPE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CARTYPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CARTYPE):
    case REQUEST(ACTION_TYPES.UPDATE_CARTYPE):
    case REQUEST(ACTION_TYPES.DELETE_CARTYPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CARTYPE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CARTYPE):
    case FAILURE(ACTION_TYPES.CREATE_CARTYPE):
    case FAILURE(ACTION_TYPES.UPDATE_CARTYPE):
    case FAILURE(ACTION_TYPES.DELETE_CARTYPE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CARTYPE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CARTYPE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CARTYPE):
    case SUCCESS(ACTION_TYPES.UPDATE_CARTYPE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CARTYPE):
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

const apiUrl = 'api/car-types';

// Actions

export const getEntities: ICrudGetAllAction<ICarType> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CARTYPE_LIST,
  payload: axios.get<ICarType>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ICarType> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CARTYPE,
    payload: axios.get<ICarType>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICarType> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CARTYPE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICarType> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CARTYPE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICarType> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CARTYPE,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
