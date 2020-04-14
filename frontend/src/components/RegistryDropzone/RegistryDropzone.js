import Dropzone from 'react-dropzone'
import React, { useState } from 'react'
import makeStyles from '@material-ui/core/styles/makeStyles'
import { RegistryDropzoneAlert } from './RegistryDropzoneAlert/RegistryDropzoneAlert'
import { RegistryDropzoneMain } from './RegistryDropzoneMain/RegistryDropzoneMain'

const useStyles = makeStyles((theme) => ({
  dropzone: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    padding: theme.spacing(4),
    marginTop: theme.spacing(4),
    marginBottom: theme.spacing(4),
    borderWidth: '2px',
    borderRadius: '2px',
    borderColor: theme.palette.text.disabled,
    borderStyle: 'dashed',
    backgroundColor: theme.palette.background.paper,
    outline: 'none',
    transition: 'border .24s ease-in-out'
  }
}))

const allowedTypes = ['application/vnd.ms-excel', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet']

export const RegistryDropzone = props => {
  const { onDrop, file } = props
  const classes = useStyles()
  const [valid, setValid] = useState(true)

  const fileDropWrapper = file => {
    if (file.length > 0) {
      setValid(true)
      onDrop(file[0])
    } else {
      setValid(false)
    }
  }

  return (
    <Dropzone
      multiple={false}
      accept={allowedTypes}
      onDrop={fileDropWrapper}>
      {({ getRootProps, getInputProps }) => (
          <>
            <RegistryDropzoneAlert disabled={valid} text={'Only xls/xlsx file format allowed for uploading!'}/>
            <div {...getRootProps({ className: classes.dropzone })}>
              <input{...getInputProps()}/>
              <RegistryDropzoneMain file={file} valid={valid}/>
            </div>
          </>
      )}
    </Dropzone>)
}
