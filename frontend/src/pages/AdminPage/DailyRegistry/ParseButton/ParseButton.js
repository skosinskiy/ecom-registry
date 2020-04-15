import React from 'react'
import IconButton from '@material-ui/core/IconButton'
import FolderRounded from '@material-ui/icons/FolderRounded'
import FolderOpenRounded from '@material-ui/icons/FolderOpenRounded'
import { makeStyles } from '@material-ui/core/styles'

const useStyles = makeStyles((theme) => ({
  downloadButton: {
    '&:hover': {
      color: theme.palette.info.light
    }
  },
  parseButton: {
    '&:hover': {
      color: theme.palette.warning.main
    }
  }
}))

export const ParseButton = props => {
  const { status, parseHandler, downloadHandler } = props
  const classes = useStyles()

  if (status === 'CREATED' || status === 'PARSE_ERROR') {
    return (
      <IconButton className={classes.parseButton} size={'small'} onClick={parseHandler}>
        <FolderOpenRounded/>
      </IconButton>
    )
  }
  if (status === 'PARSED') {
    return (
      <IconButton
        className={classes.downloadButton}
        size={'small'}
        onClick={downloadHandler}>
        <FolderRounded/>
      </IconButton>
    )
  }
  return (
    <IconButton disabled size={'small'}>
      <FolderRounded />
    </IconButton>
  )
}