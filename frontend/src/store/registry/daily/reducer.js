import * as TYPES from './types'

const initialState = {
  registryList: null,
  isLoading: false
}

const usersReducer = (state = initialState, action) => {
  switch (action.type) {
    case TYPES.DAILY_REGISTRY_LOADING:
      return {
        ...state,
        isLoading: action.payload
      }
    case TYPES.DAILY_REGISTRY_FETCHED:
      return {
        ...state,
        registryList: action.payload
      }
    case TYPES.DAILY_REGISTRY_DELETED:
      return {
        ...state,
        registryList: state.registryList.filter(registry => registry.id !== action.payload.id)
      }
    case TYPES.DAILY_REGISTRY_UPLOADED:
      return {
        ...state,
        registryList: state.registryList.concat(action.payload)
      }
    default:
      return {...state}
  }
}

export default usersReducer