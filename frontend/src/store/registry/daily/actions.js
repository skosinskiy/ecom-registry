import * as TYPES from './types'

export const dailyRegistryLoading = isLoading => ({
  type: TYPES.DAILY_REGISTRY_LOADING,
  payload: isLoading
})

export const dailyRegistryFetched = dailyRegistryList => ({
  type: TYPES.DAILY_REGISTRY_FETCHED,
  payload: dailyRegistryList
})

export const dailyRegistryDeleted = dailyRegistry => ({
  type: TYPES.DAILY_REGISTRY_DELETED,
  payload: dailyRegistry
})

export const dailyRegistryUploaded = dailyRegistry => ({
  type: TYPES.DAILY_REGISTRY_UPLOADED,
  payload: dailyRegistry
})