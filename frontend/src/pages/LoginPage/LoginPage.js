import React, { Component } from 'react'
import Avatar from '@material-ui/core/Avatar'
import Button from '@material-ui/core/Button'
import TextField from '@material-ui/core/TextField'
import Link from '@material-ui/core/Link'
import Paper from '@material-ui/core/Paper'
import Grid from '@material-ui/core/Grid'
import LockOutlinedIcon from '@material-ui/icons/LockOutlined'
import Typography from '@material-ui/core/Typography'
import BackgroundImage from '../../img/LoginPage/background.jpg'
import { connect } from 'react-redux'
import { usersOperations } from '../../store/users'
import { Redirect } from 'react-router-dom'
import withStyles from '@material-ui/core/styles/withStyles'
import { Preloader } from '../../components/Preloader/Preloader'

const styles = theme => ({
  root: {
    height: '100vh'
  },
  image: {
    backgroundImage: `url(${BackgroundImage})`,
    backgroundRepeat: 'no-repeat',
    backgroundSize: 'cover',
    backgroundPosition: 'center'
  },
  imageWrapper: {
    width: '100%',
    height: '100vh',
    display: 'flex',
    justifyContent: 'center',
    flexDirection: 'column',
    backgroundColor: 'rgba(0, 0, 0, 0.7)'
  },
  imageTitle: {
    margin: '0 0 0 150px',
    color: 'white',
    fontSize: '40px',
    fontWeight: 700
  },
  imageText: {
    margin: '0 0 0 150px',
    color: 'white',
    fontSize: '25px'
  },
  paper: {
    margin: theme.spacing(8, 4),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center'
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.primary.main
  },
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(1)
  },
  submit: {
    margin: theme.spacing(3, 0, 2)
  },
  forgot: {
    margin: theme.spacing(0, 0, 2)
  }
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

  render () {
    const { submitLoginForm, currentUser, isCurrentUserLoading, classes } = this.props
    const { email, password } = this.state

    if (isCurrentUserLoading) {
      return <Preloader/>
    }

    if (currentUser) {
      return <Redirect to={'/'}/>
    }

    return (
      <Grid container className={classes.root}>
        <Grid item xs={1} sm={6} md={8} lg={9} className={classes.image}>
          <div className={classes.imageWrapper}>
            <h2 className={classes.imageTitle}>Welcome to E-commerce registry helper!</h2>
            <p className={classes.imageText}>This application allows easily deal with everyday registry routine!</p>
          </div>
        </Grid>
        <Grid item xs={11} sm={6} md={4} lg={3} component={Paper} elevation={6} square>
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
              <Button
                type="submit"
                fullWidth
                variant="contained"
                color="primary"
                disabled={email.length < 3 || password.length < 3}
                className={classes.submit}
              >
                  Sign In
              </Button>
              <Button
                fullWidth
                variant="contained"
                color="secondary"
                className={classes.forgot}
              >
                  Forgot Password
              </Button>
              <Grid container direction={'column'} alignItems={'center'}>
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
    )
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