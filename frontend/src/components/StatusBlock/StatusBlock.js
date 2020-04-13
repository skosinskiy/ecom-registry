import makeStyles from '@material-ui/core/styles/makeStyles'
import React from 'react'

const useStyles = makeStyles({
  root: {
    color: props => props.colors.color,
    backgroundColor: props => props.colors.backgroundColor,
    height: '20px',
    padding: '4px 8px',
    flexGrow: 0,
    minWidth: '20px'
  }
})

export const StatusBlock = props => {
  const { text } = props
  const classes = useStyles(props)
  return (
    <span className={classes.root}>
      {text}
    </span>
  )
}