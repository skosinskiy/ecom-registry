import * as ACTIONS from './actions'
import api from '../../components/Axios/Axios'
import {toastr} from 'react-redux-toastr'

export const submitLoginForm = (event, email, password) => dispatch => {
  event.preventDefault()
  dispatch(ACTIONS.currentUserLoading(true))

  const data = {email, password}

  api.post('/api/auth', data)
  .then(res => {
    if (res.status === 200) {
      const {jwtAccessToken, jwtRefreshToken, jwtRefreshTokenExpireDate} = res.data
      window.localStorage.setItem('jwt_access_token', jwtAccessToken);
      window.localStorage.setItem('jwt_refresh_token', jwtRefreshToken);
      window.localStorage.setItem('jwt_refresh_token_expire', jwtRefreshTokenExpireDate)
      return dispatch(getCurrentUser())
    }
  })
  .catch(() => {
    dispatch(ACTIONS.currentUserLoading(false))
    toastr.error('Error', 'Wrong password or email!')
  })
}

export const getCurrentUser = () => dispatch => {
  dispatch(ACTIONS.currentUserLoading(true))
  return api.get('/api/users/current')
    .then(res => dispatch(ACTIONS.currentUserFetched(res.data)))
    .catch(reason => toastr.error('Error', reason))
    .finally(() => {
      dispatch(ACTIONS.currentUserLoading(false))
  })
}