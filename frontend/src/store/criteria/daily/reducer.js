import * as TYPES from './types'

const initialState = {
  isLoading: false,
  criteriaList: [],
  totalElements: 0,
  page: 0
}

const dailyRegistryParseCriteriaReducer = (state = initialState, action) => {
  switch (action.type) {
    case TYPES.DAILY_REGISTRY_PARSE_CRITERIA_LOADING:
      return {
        ...state,
        isLoading: action.payload
      }
    case TYPES.DAILY_REGISTRY_PARSE_CRITERIA_FETCHED:
      return {
        ...state,
        criteriaList: action.payload.content,
        totalElements: action.payload.totalElements
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

export default dailyRegistryParseCriteriaReducer