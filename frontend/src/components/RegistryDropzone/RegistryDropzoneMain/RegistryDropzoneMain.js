import ExcelIcon from '../../../icons/excel.svg'
import React from 'react'
import CloudUploadIcon from '@material-ui/icons/CloudUpload'
import makeStyles from '@material-ui/core/styles/makeStyles'

const useStyles = makeStyles((theme) => ({
  main: {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    color: theme.palette.text.secondary
  },
  image: {
    height: '40px',
    width: '40px'
  }
}))

export const RegistryDropzoneMain = props => {
  const {file, valid} = props
  const classes = useStyles()

  if (file && valid) {
    return (
      <div className={classes.main}>
        <img className={classes.image} src={ExcelIcon} alt={''}/>
        <p>{file.name}</p>
      </div>
    )
  }
  return (
    <div className={classes.main}>
      <CloudUploadIcon className={classes.image}/>
      <p>Upload File</p>
    </div>
  )
}