import {combineReducers} from 'redux'
import {reducer as toastrReducer} from 'react-redux-toastr'

import users from './users'
import dailyRegistry from './registry/daily'
import dailyRegistryParseCriteria from './criteria/daily'

const rootReducer = combineReducers({
  users: users,
  dailyRegistry: dailyRegistry,
  dailyRegistryParseCriteria: dailyRegistryParseCriteria,
  toastr: toastrReducer
})

export default rootReducer