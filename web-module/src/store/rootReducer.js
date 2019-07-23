import {combineReducers} from 'redux'
import {reducer as toastrReducer} from 'react-redux-toastr'

import users from './users'

const rootReducer = combineReducers({
    users: users,
    toastr: toastrReducer
})

export default rootReducer