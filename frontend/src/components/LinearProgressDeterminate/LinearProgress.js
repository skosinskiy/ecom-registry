import React from 'react'
import DialogContentText from '@material-ui/core/DialogContentText'
import LinearProgress from '@material-ui/core/LinearProgress'
import makeStyles from '@material-ui/core/styles/makeStyles'

const useStyles = makeStyles({
  colorPrimary: {
    backgroundColor: '#cbe8cc'
  },
  barColorPrimary: {
    backgroundColor: '#73c176'
  }
})

export const LinearProgressDeterminate = props => {
  const {disabled, progress} = props
  const classes = useStyles()

  if (disabled) {
    return null
  }

  return (
    <div>
      <DialogContentText>
          Uploading file
      </DialogContentText>
      <LinearProgress
        classes={{colorPrimary: classes.colorPrimary, barColorPrimary: classes.barColorPrimary}}
        variant="determinate"
        value={progress} />
    </div>
  )
}