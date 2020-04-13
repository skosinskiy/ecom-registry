import React from 'react'
import Drawer from '@material-ui/core/Drawer'
import AppBar from '@material-ui/core/AppBar'
import Toolbar from '@material-ui/core/Toolbar'
import Divider from '@material-ui/core/Divider'
import IconButton from '@material-ui/core/IconButton'
import {withRouter} from 'react-router-dom'
import classNames from 'classnames'
import ExitToAppRoundedIcon from '@material-ui/icons/ExitToAppRounded'
import SidebarMenu from './SidebarMenu/SidebarMenu'
import AdminRoutes from '../../components/AdminRoutes/AdminRoutes'
import makeStyles from '@material-ui/core/styles/makeStyles'

const drawerWidth = 240

const useStyles = makeStyles((theme) => ({
  root: {
    display: 'flex'
  },
  toolbar: {
    display: 'flex',
    justifyContent: 'flex-end'
  },
  toolbarText: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    padding: '0 8px',
    ...theme.mixins.toolbar
  },
  drawerPaper: {
    position: 'relative',
    whiteSpace: 'nowrap',
    width: drawerWidth,
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen
    })
  },
  appBarSpacer: theme.mixins.toolbar,
  content: {
    flexGrow: 1,
    padding: theme.spacing(3),
    height: '100vh',
    overflow: 'auto'
  }
}))

const AdminPage = () => {
  const classes = useStyles()

  const logoutUser = () => {
    window.localStorage.clear()
    window.location.reload()
  }

  return (
    <div className={classes.root}>
      <AppBar
        position="absolute"
        className={classNames(classes.appBar, classes.appBarShift)}
      >
        <Toolbar className={classes.toolbar}>
          <IconButton onClick={logoutUser} color="inherit" alt="Log out">
            <ExitToAppRoundedIcon/>
          </IconButton>
        </Toolbar>
      </AppBar>
      <Drawer
        variant="permanent"
        classes={{
          paper: classNames(classes.drawerPaper)
        }}
      >
        <div className={classes.toolbarText}>
              Hello!
        </div>
        <Divider/>
        <SidebarMenu/>
        <Divider/>
      </Drawer>
      <main className={classes.content}>
        <div className={classes.appBarSpacer}/>
        <AdminRoutes/>
      </main>
    </div>
  )
}

export default withRouter(AdminPage)