import * as ACTIONS from './actions'
import api from '../../components/Axios/Axios'
import {toastr} from 'react-redux-toastr'

export const submitLoginForm = event => dispatch => {
    event.preventDefault()
    dispatch(ACTIONS.currentUserLoading(true))

    const data = new FormData(event.target)
    api.post('/auth', data).then(res => {
        if (res.status === 200) {
            dispatch(getCurrentUser())
        }
    })
        .catch(() => {
            dispatch(ACTIONS.currentUserLoading(false))
            toastr.error('Error', 'Wrong password or email!')
        })
}

export const getCurrentUser = () => dispatch => {
    dispatch(ACTIONS.currentUserLoading(true))
    api.get('/api/users/current').then(user => {
        if (user !== '') {
            dispatch(ACTIONS.currentUserFetched(user))
        }
    })
        .finally(() => {
            dispatch(ACTIONS.currentUserLoading(false))
        })
}