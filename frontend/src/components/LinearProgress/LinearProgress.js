import React from 'react'
import { makeStyles } from '@material-ui/core/styles'
import LinearProgress from '@material-ui/core/LinearProgress'

const useStyles = makeStyles((theme) => ({
  root: {
    height: '4px',
    boxSizing: 'border-box',
    width: '100%',
    '& > * + *': {
      marginTop: theme.spacing(2)
    }
  }
}))

export const LinearIndeterminate = props => {
  const classes = useStyles()
  const { disabled, color = 'secondary' } = props

  const progress = disabled ? null : <LinearProgress color={color}/>

  return (
    <div className={classes.root}>
      {progress}
    </div>
  )
}