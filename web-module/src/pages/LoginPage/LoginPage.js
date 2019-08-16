import React, {Component} from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Checkbox from '@material-ui/core/Checkbox';
import Link from '@material-ui/core/Link';
import Paper from '@material-ui/core/Paper';
import Grid from '@material-ui/core/Grid';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import BackgroundImage from '../../img/LoginPage/background.jpg'
import {connect} from "react-redux";
import {usersOperations} from '../../store/users'
import {Redirect} from "react-router-dom";
import Preloader from "../../components/Preloader/Preloader";
import withStyles from "@material-ui/core/styles/withStyles";

const styles = theme => ({
  root: {
    height: '100vh'
  },
  image: {
    backgroundImage: `url(${BackgroundImage})`,
    backgroundRepeat: 'no-repeat',
    backgroundSize: 'cover',
    backgroundPosition: 'center',
  },
  paper: {
    margin: theme.spacing(8, 4),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.primary.main,
  },
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
})

class LoginForm extends Component {

  state = {
    email: '',
    password: ''
  }

  handleChange = (event, propName) => {
    this.setState({
      [propName]: event.target.value
    })
  }

  render() {
    const {submitLoginForm, currentUser, isCurrentUserLoading, classes} = this.props
    const {email, password} = this.state

    if (isCurrentUserLoading) {
      return <Preloader/>
    }

    if (currentUser) {
      return <Redirect to={'/'}/>
    }

    return (
        <Grid container className={classes.root}>
          <Grid item xs={false} sm={4} md={9} className={classes.image}/>
          <Grid item xs={12} sm={8} md={3} component={Paper} elevation={6} square>
            <div className={classes.paper}>
              <Avatar className={classes.avatar}>
                <LockOutlinedIcon/>
              </Avatar>
              <Typography component="h1" variant="h5">
                Login to your account
              </Typography>
              <form className={classes.form} noValidate onSubmit={event => submitLoginForm(event, email, password)}>
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    id="email"
                    label="Email Address"
                    name="username"
                    autoComplete="email"
                    autoFocus
                    onChange={event => this.handleChange(event, 'email')}
                    value={email}
                />
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    name="password"
                    label="Password"
                    type="password"
                    id="password"
                    autoComplete="current-password"
                    onChange={event => this.handleChange(event, 'password')}
                    value={password}
                />
                <FormControlLabel
                    control={<Checkbox value="remember" color="primary"/>}
                    label="Remember me"
                />
                <Button
                    type="submit"
                    fullWidth
                    variant="contained"
                    color="primary"
                    className={classes.submit}
                >
                  Sign In
                </Button>
                <Grid container>
                  <Grid item xs>
                    <Link href="#" variant="body2">
                      Forgot password?
                    </Link>
                  </Grid>
                  <Grid item>
                    <Link href="#" variant="body2">
                      {"Don't have an account? Sign Up"}
                    </Link>
                  </Grid>
                </Grid>
              </form>
            </div>
          </Grid>
        </Grid>
    );
  }

}

const mapStateToProps = (state) => ({
  currentUser: state.users.currentUser,
  isCurrentUserLoading: state.users.isCurrentUserLoading
})

const mapDispatchToProps = dispatch => ({
  submitLoginForm: (event, email, password) => dispatch(usersOperations.submitLoginForm(event, email, password))
})

export default connect(mapStateToProps, mapDispatchToProps)(withStyles(styles)(LoginForm))