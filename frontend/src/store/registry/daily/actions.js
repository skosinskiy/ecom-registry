import * as TYPES from './types'

export const dailyRegistryLoading = isLoading => ({
  type: TYPES.DAILY_REGISTRY_LOADING,
  payload: isLoading
})

export const dailyRegistryFetched = dailyRegistryList => ({
  type: TYPES.DAILY_REGISTRY_FETCHED,
  payload: dailyRegistryList
})

export const dailyRegistryParsed = dailyRegistry => ({
  type: TYPES.DAILY_REGISTRY_PARSING,
  payload: dailyRegistry
})

export const pageUpdated = page => ({
  type: TYPES.PAGE_UPDATED,
  payload: page
})