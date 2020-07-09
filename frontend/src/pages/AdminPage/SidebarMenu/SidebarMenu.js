import React, {Component} from 'react'
import ListItem from '@material-ui/core/ListItem'
import ListItemIcon from '@material-ui/core/ListItemIcon'
import ListItemText from '@material-ui/core/ListItemText'
import {Grant} from '../../../constants/permissions'
import {connect} from 'react-redux'
import PropTypes from 'prop-types'
import DescriptionOutlinedIcon from '@material-ui/icons/DescriptionOutlined'
import {hasGrant} from '../../../utils/hasGrant'
import {CustomNavLink} from '../../../components/NavLink/NavLink'

class SidebarMenu extends Component {
  render () {
    const { user } = this.props

    return (
      <div>

        {
          hasGrant(user, Grant.MANAGE_REGISTRY) &&
          <CustomNavLink to={'/daily-registry'}>
            <ListItem button alignItems={'center'}>
              <ListItemIcon>
                <DescriptionOutlinedIcon/>
              </ListItemIcon>
              <ListItemText primary={'Daily registry'}/>
            </ListItem>
          </CustomNavLink>
        }

        {
          hasGrant(user, Grant.MANAGE_CRITERIA) &&
          <CustomNavLink to={'/daily-registry/parse/criteria'}>
            <ListItem button alignItems={'center'}>
              <ListItemIcon>
                <DescriptionOutlinedIcon/>
              </ListItemIcon>
              <ListItemText primary={'Parse criteria'}/>
            </ListItem>
          </CustomNavLink>
        }

      </div>
    )
  }
}

SidebarMenu.propTypes = {
  user: PropTypes.object.isRequired
}

const mapStateToProps = state => {
  return {
    user: state.users.currentUser
  }
}

export default connect(mapStateToProps)(SidebarMenu)