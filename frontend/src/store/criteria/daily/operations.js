import * as ACTIONS from '../../criteria/daily/actions'
import api from '../../../components/Axios/Axios'

export const fetchDailyRegistryParseCriteria = page => dispatch => {
  dispatch(ACTIONS.dailyRegistryParseCriteriaLoading(true))
  api.get(`/api/parse/criteria/daily?page=${page}&size=10&sort=name`, null)
    .then(page => {
      dispatch(ACTIONS.dailyRegistryParseCriteriaFetched(page))
      dispatch(ACTIONS.dailyRegistryParseCriteriaLoading(false))
    })
}

export const updatePage = page => dispatch => {
  dispatch(ACTIONS.pageUpdated(page))
}