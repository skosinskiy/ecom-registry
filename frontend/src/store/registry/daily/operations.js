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

export const uploadDailyRegistry = (date, file) => dispatch => {
  dispatch(ACTIONS.dailyRegistryLoading(true))
  const formData = new FormData()
  formData.append('date', getDateString(date))
  formData.append('file', file)
  api.post('/api/registry/daily', formData)
    .then(registry => {
      dispatch(ACTIONS.dailyRegistryUploaded(registry))
      dispatch(ACTIONS.dailyRegistryLoading(false))
      toastr.success(`Successfully uploaded registry for ${getDateString(date)}`)
    })
    .catch(() => {
      dispatch(ACTIONS.dailyRegistryLoading(false))
    })
}
