import React from 'react'
import PropTypes from 'prop-types'
import CircularProgress from '@material-ui/core/CircularProgress'
import makeStyles from '@material-ui/core/styles/makeStyles'

const useStyles = makeStyles((theme) => ({
  preloader: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    width: '100%',
    height: '100vh',
    backgroundColor: theme.palette.background.default
  }
}))

export const Preloader = props => {
  const classes = useStyles()
  return (
    <div className={classes.preloader}>
      <CircularProgress />
    </div>
  )
}

Preloader.propTypes = {
  classes: PropTypes.object.isRequired
}