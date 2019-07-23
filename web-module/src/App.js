import React, {Component} from 'react';
import AppRoutes from './components/AppRoutes/AppRoutes'
import {withRouter} from "react-router-dom";
import CssBaseline from "@material-ui/core/CssBaseline";
import {connect} from "react-redux";
import {usersOperations} from './store/users'
import ToastrMessage from "./components/ToastrMessage/ToastrMessage";
import Preloader from "./components/Preloader/Preloader";

class App extends Component {
    componentDidMount () {
        this.props.getCurrentUser()
    }

    render() {

        if (this.props.isCurrentUserLoading) {
            return <Preloader/>
        }

        return (
            <>
                <CssBaseline/>
                <AppRoutes/>
                <ToastrMessage/>
            </>
        );
    }

}

const mapStateToProps = state => {
    return {
        currentUser: state.users.currentUser,
        isCurrentUserLoading: state.users.isCurrentUserLoading
    }
}

const mapDispatchToProps = dispatch => ({
    getCurrentUser: () => dispatch(usersOperations.getCurrentUser())
})

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(App));
