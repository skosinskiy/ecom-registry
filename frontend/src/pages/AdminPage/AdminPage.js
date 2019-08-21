import React, {Component} from 'react';
import Drawer from '@material-ui/core/Drawer';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Divider from '@material-ui/core/Divider';
import IconButton from '@material-ui/core/IconButton';
import {withStyles} from "@material-ui/core";
import {withRouter} from "react-router-dom";
import classNames from 'classnames'
import PowerSetting from '@material-ui/icons/PowerSettingsNew'
import SidebarMenu from './SidebarMenu/SidebarMenu'
import AdminRoutes from '../../components/AdminRoutes/AdminRoutes'

const drawerWidth = 240;

const styles = theme => ({
  root: {
    display: 'flex'
  },
  toolbar: {
    paddingRight: 24
  },
  toolbarText: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    padding: '0 8px',
    ...theme.mixins.toolbar
  },
  appBar: {
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen
    })
  },
  appBarShift: {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen
    })
  },
  menuButton: {
    marginLeft: 12,
    marginRight: 36
  },
  menuButtonHidden: {
    display: 'none'
  },
  title: {
    flexGrow: 1
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
  drawerPaperClose: {
    overflowX: 'hidden',
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen
    }),
    width: theme.spacing(7),
    [theme.breakpoints.up('sm')]: {
      width: theme.spacing(9)
    }
  },
  appBarSpacer: theme.mixins.toolbar,
  content: {
    flexGrow: 1,
    padding: theme.spacing(3),
    height: '100vh',
    overflow: 'auto'
  },
  chartContainer: {
    marginLeft: -22
  },
  tableContainer: {
    height: 320
  },
  h5: {
    marginBottom: theme.spacing(2)
  }
})

class AdminPage extends Component {

  static logoutUser() {
    window.localStorage.clear()
    window.location.reload()
  }

  render() {

    const {classes} = this.props

    return (
        <div className={classes.root}>
          <AppBar
              position="absolute"
              className={classNames(classes.appBar, classes.appBarShift)}
          >
            <Toolbar className={classes.toolbar}>
              <Typography
                  component="h1"
                  variant="h6"
                  color="inherit"
                  noWrap
                  className={classes.title}
              >
                Admin Panel
              </Typography>
              <IconButton onClick={AdminPage.logoutUser} color="inherit" alt="Log out">
                <PowerSetting/>
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
    );
  }
}

export default withRouter(withStyles(styles, {withTheme: true})(AdminPage))