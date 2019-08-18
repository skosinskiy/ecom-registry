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
import AppRoutes from "../../components/AppRoutes/AppRoutes";

const drawerWidth = 240;

const styles = theme => ({
  root: {
    display: 'flex',
  },
  toolbar: {
    paddingRight: 24, // keep right padding when drawer closed
  },
  toolbarIcon: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: '0 8px',
    ...theme.mixins.toolbar,
  },
  appBar: {
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
  },
  appBarShift: {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  menuButton: {
    marginRight: 36,
  },
  menuButtonHidden: {
    display: 'none',
  },
  title: {
    flexGrow: 1,
  },
  drawerPaper: {
    position: 'relative',
    whiteSpace: 'nowrap',
    width: drawerWidth,
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
    backgroundColor: theme.palette.grey["900"]
  },
  appBarSpacer: theme.mixins.toolbar,
  content: {
    flexGrow: 1,
    height: '100vh',
    overflow: 'auto',
  },
  container: {
    paddingTop: theme.spacing(4),
    paddingBottom: theme.spacing(4),
  },
  paper: {
    padding: theme.spacing(2),
    display: 'flex',
    overflow: 'auto',
    flexDirection: 'column',
  },
  fixedHeight: {
    height: 240,
  },
});

class AdminPage extends Component {

  static logoutUser() {
    window.localStorage.clear()
    window.location.reload()
  }

  render() {

    const {classes} = this.props

    return (
        <div className={classes.root}>
          <AppBar position="absolute" className={classNames(classes.appBar, classes.appBarShift)}>
            <Toolbar className={classes.toolbar}>
              <Typography
                  component="h1"
                  variant="h6"
                  color="inherit"
                  noWrap
                  className={classes.title}
              >
                E-commerce Admin Panel
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
            <SidebarMenu/>
            <Divider/>
          </Drawer>
          <main className={classes.content}>
            <div className={classes.appBarSpacer}/>
            {/*<AdminRoutes/>*/}
          </main>
        </div>
    );
  }
}

export default withRouter(withStyles(styles, {withTheme: true})(AdminPage))