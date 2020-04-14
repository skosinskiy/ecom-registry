import * as TYPES from './types'

const initialState = {
  registryList: [],
  isLoading: false,
  totalElements: 0,
  page: 0
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
        registryList: action.payload.content,
        totalElements: action.payload.totalElements
      }
    case TYPES.DAILY_REGISTRY_PARSING:
      return {
        ...state,
        registryList: state.registryList.map(registry => {
          if (registry.id === action.payload.id) {
            registry.status = action.payload.status
          }
          return registry
        })
      }
    case TYPES.PAGE_UPDATED:
      return {
        ...state,
        page: action.payload
      }
    default:
      return { ...state }
  }
}

export default usersReducer