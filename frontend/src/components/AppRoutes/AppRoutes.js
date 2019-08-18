import React from 'react'
import {Redirect, Route, Switch, withRouter} from 'react-router-dom'
import LoginPage from '../../pages/LoginPage/LoginPage'
import * as PropTypes from "prop-types";
import AdminPage from "../../pages/AdminPage/AdminPage";
import {connect} from "react-redux";

const AppRoutes = props => {
    const {currentUser} = props

    return (
        <Switch>
            <Route path="/login" component={LoginPage}/>
            <ProtectedRoute path="/" component={AdminPage} authenticated={!!currentUser}/>
        </Switch>
    )
}

export const ProtectedRoute = ({component: Component, authenticated, ...rest}) => (
    <Route
        {...rest}
        render={props => authenticated ? <Component {...props} /> : <Redirect to="/login"/>}
    />
)

const mapStateToProps = state => {
    return {
        currentUser: state.users.currentUser
    }
}

AppRoutes.propTypes = {
    currentUser: PropTypes.object
}

ProtectedRoute.propTypes = {
    component: PropTypes.func.isRequired,
    authenticated: PropTypes.bool
}

export default withRouter(connect(mapStateToProps)(AppRoutes))