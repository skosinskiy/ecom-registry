import * as ACTIONS from './actions'
import api from '../../components/Axios/Axios'
import {toastr} from 'react-redux-toastr'
import {setLocalStorageTokens} from '../../service/jwtService'

export const submitLoginForm = (event, email, password) => dispatch => {
  event.preventDefault()
  dispatch(ACTIONS.currentUserLoading(true))

  const data = {email, password}

  return api.post('/api/auth', data).then(res => {
    setLocalStorageTokens(res)
    return dispatch(getCurrentUser())
  })
    .catch(error => {
      dispatch(ACTIONS.currentUserLoading(false))
      window.localStorage.clear()
      toastr.error('Login error', error.response.data.message)
    })
}

export const getCurrentUser = () => dispatch => {
  dispatch(ACTIONS.currentUserLoading(true))
  return api.get('/api/users/current')
    .then(res => dispatch(ACTIONS.currentUserFetched(res)))
    .catch(reason => toastr.error('Error', reason))
    .finally(() => {
      dispatch(ACTIONS.currentUserLoading(false))
    })
}