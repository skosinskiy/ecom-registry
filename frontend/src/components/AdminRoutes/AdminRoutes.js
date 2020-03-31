import React, {Component} from 'react'
import {Redirect, Route, Switch, withRouter} from 'react-router-dom'
import {connect} from 'react-redux'
import PropTypes from 'prop-types'
import {Grant} from '../../constants/permissions'
import {hasGrant} from '../../utils/hasGrant'
import {DailyRegistry} from '../../pages/AdminPage/DailyRegistry/DailyRegistry'

class AdminRouter extends Component {
  render () {
    const {user} = this.props
    return (
      <Switch>
        <AuthorizedRoute authorized={hasGrant(user, Grant.MANAGE_REGISTRY)} path="/daily-registry" component={DailyRegistry} />
      </Switch>
    )
  }
}

AdminRouter.propTypes = {
  user: PropTypes.object.isRequired
}

export const AuthorizedRoute = ({component: Component, authorized, ...rest}) => (
  <Route {...rest} render={(props) => authorized
    ? <Route component={Component} {...props} />
    : <Redirect to='/login' />} />
)

AuthorizedRoute.propTypes = {
  component: PropTypes.func.isRequired,
  authorized: PropTypes.bool.isRequired
}

const mapStateToProps = ({users}) => {
  return {
    user: users.currentUser
  }
}

export default withRouter(connect(mapStateToProps)(AdminRouter))