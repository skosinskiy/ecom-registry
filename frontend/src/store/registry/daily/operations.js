import * as ACTIONS from './actions'
import api from '../../../components/Axios/Axios'
import {getDateString} from '../../../utils/dateUtils'
import {toastr} from 'react-redux-toastr'

export const fetchDailyRegistry = () => dispatch => {
  dispatch(ACTIONS.dailyRegistryLoading(true))
  api.get('/api/registry/daily', null)
    .then(page => {
      dispatch(ACTIONS.dailyRegistryFetched(page.content))
      dispatch(ACTIONS.dailyRegistryLoading(false))
    })
    .catch(() => {
      dispatch(ACTIONS.dailyRegistryLoading(false))
    })
}

export const deleteDailyRegistry = registryId => dispatch => {
  dispatch(ACTIONS.dailyRegistryLoading(true))
  api.delete(`/api/registry/daily/${registryId}`)
    .then(registry => {
      dispatch(ACTIONS.dailyRegistryDeleted(registry))
      dispatch(ACTIONS.dailyRegistryLoading(false))
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

export const uploadDailyRegistry = (date, file, uploadHandler) => dispatch => {
  const formData = new FormData()
  formData.append('date', getDateString(date))
  formData.append('file', file)
  api.postFile('/api/registry/daily', formData, null, uploadHandler)
    .then(registry => {
      dispatch(ACTIONS.dailyRegistryUploaded(registry))
      toastr.success(`Successfully uploaded registry for ${getDateString(date)}`)
    })
}
