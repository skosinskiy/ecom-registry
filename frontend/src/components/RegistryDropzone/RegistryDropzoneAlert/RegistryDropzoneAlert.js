import Alert from '@material-ui/lab/Alert'
import React from 'react'
import makeStyles from '@material-ui/core/styles/makeStyles'

const useStyles = makeStyles((theme) => ({
  alert: {
    marginBottom: theme.spacing(-2)
  }
}))

export const RegistryDropzoneAlert = props => {
  const { disabled, text } = props
  const classes = useStyles()

  if (disabled) {
    return null
  }

  return (
    <Alert className={classes.alert} severity="error">
      {text}
    </Alert>
  )
}