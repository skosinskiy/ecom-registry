import React, {Component} from 'react'
import {NavLink} from 'react-router-dom'
import ListItem from '@material-ui/core/ListItem'
import ListItemIcon from '@material-ui/core/ListItemIcon'
import ListItemText from '@material-ui/core/ListItemText'
import BusinessIcon from '@material-ui/icons/Store'
import BusinessCategoryIcon from '@material-ui/icons/BusinessCenter'
import {Grant} from '../../../constants/permissions'
import {connect} from 'react-redux'
import './sidebar-menu.scss'
import PropTypes from 'prop-types'

const hasGrant = (user, grant) => {
    return user.permissions.includes(grant)
}

class SidebarMenu extends Component {

    render() {

        const {user} = this.props

        return (
            <div className={'sidebar'}>
                {
                    <div className={'header'}>
                        <p className={'header-name'} >{ `${user.firstName} ${user.lastName}`}</p>
                        <p className={'header-email'}>{ `${user.email}`}</p>
                    </div>
                }

                {
                    hasGrant(user, Grant.MANAGE_ACCOUNTS) &&
                    <NavLink to={'/admin/businesses'} className="sidebarItem">
                        <ListItem button>
                            <ListItemIcon>
                                <BusinessIcon/>
                            </ListItemIcon>
                            <ListItemText primary="Businesses"/>
                        </ListItem>
                    </NavLink>
                }

                {
                    hasGrant(user, Grant.MANAGE_REGISTRY) &&
                    <NavLink to={'/admin/business-categories'} className="sidebarItem">
                        <ListItem button>
                            <ListItemIcon>
                                <BusinessCategoryIcon/>
                            </ListItemIcon>
                            <ListItemText primary={'Business Categories'}/>
                        </ListItem>
                    </NavLink>
                }

            </div>
        )
    }
}

SidebarMenu.propTypes = {
    user: PropTypes.object.isRequired,
}

const mapStateToProps = state => {
    return {
        user: state.users.currentUser
    }
}

export default connect(mapStateToProps)(SidebarMenu)