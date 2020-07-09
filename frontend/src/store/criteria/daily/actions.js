import * as TYPES from '../../criteria/daily/types'

export const dailyRegistryParseCriteriaLoading = isLoading => ({
  type: TYPES.DAILY_REGISTRY_PARSE_CRITERIA_LOADING,
  payload: isLoading
})

export const dailyRegistryParseCriteriaFetched = dailyRegistryParseCriteriaList => ({
  type: TYPES.DAILY_REGISTRY_PARSE_CRITERIA_FETCHED,
  payload: dailyRegistryParseCriteriaList
})

export const pageUpdated = page => ({
  type: TYPES.PAGE_UPDATED,
  payload: page
})
