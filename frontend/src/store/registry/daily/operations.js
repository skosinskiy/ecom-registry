import * as ACTIONS from './actions'
import api from '../../../components/Axios/Axios'
import { getDateString } from '../../../utils/dateUtils'
import { toastr } from 'react-redux-toastr'

export const fetchDailyRegistry = (date, page) => dispatch => {
  dispatch(ACTIONS.dailyRegistryLoading(true))
  api.get(`/api/registry/daily?page=${page}&size=10&sort=registryDate&year=${date.getFullYear()}&month=${date.getMonth() + 1}`, null)
    .then(page => {
      dispatch(ACTIONS.dailyRegistryFetched(page))
      dispatch(ACTIONS.dailyRegistryLoading(false))
    })
}

export const deleteDailyRegistry = (registryId, date, page, decreasePage) => dispatch => {
  dispatch(ACTIONS.dailyRegistryLoading(true))
  api.delete(`/api/registry/daily/${registryId}`)
    .then(registry => {
      if (decreasePage) {
        dispatch(ACTIONS.pageUpdated(page - 1))
      } else {
        dispatch(fetchDailyRegistry(date, page))
      }
      toastr.success(`Successfully deleted registry for ${registry.registryDate}`)
    })
    .catch(() => {
      dispatch(ACTIONS.dailyRegistryLoading(false))
    })
}

export const parseDailyRegistry = registryId => dispatch => {
  api.get(`/api/registry/daily/parse/${registryId}`)
    .then(registry => {
      dispatch(ACTIONS.dailyRegistryParsed(registry))
      toastr.success(`Successfully added registry for ${registry.registryDate} to processing`)
    })
}

export const uploadDailyRegistry = (registryDate, file, uploadHandler, date, page, increasePage) => dispatch => {
  const formData = new FormData()
  formData.append('date', getDateString(registryDate))
  formData.append('file', file)
  api.postFile('/api/registry/daily', formData, null, uploadHandler)
    .then(() => {
      dispatch(ACTIONS.dailyRegistryLoading(true))
      if (increasePage) {
        dispatch(ACTIONS.pageUpdated(page + 1))
      } else {
        dispatch(fetchDailyRegistry(date, page))
      }
      uploadHandler(100, true)
      toastr.success(`Successfully uploaded registry for ${getDateString(registryDate)}`)
    }).catch(() => uploadHandler(0, true))
}

export const updatePage = page => dispatch => {
  dispatch(ACTIONS.pageUpdated(page))
}
