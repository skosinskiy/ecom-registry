import React, {useState} from 'react'
import DialogTitle from '@material-ui/core/DialogTitle'
import DialogContent from '@material-ui/core/DialogContent'
import DialogContentText from '@material-ui/core/DialogContentText'
import {RegistryDropzone} from '../../../../components/RegistryDropzone/RegistryDropzone'
import DialogActions from '@material-ui/core/DialogActions'
import Button from '@material-ui/core/Button'
import Dialog from '@material-ui/core/Dialog'
import {DatePicker} from './DatePicker/DatePicker'
import {useDispatch} from 'react-redux'
import {uploadDailyRegistry} from '../../../../store/registry/daily/operations'
import {LinearProgressDeterminate} from '../../../../components/LinearProgressDeterminate/LinearProgress'

export const AddNewModal = props => {
  const { isOpen, handleClose } = props
  const [file, setFile] = useState(null)
  const [date, setDate] = useState(new Date())
  const [dateParsed, setDateParsed] = useState(false)
  const [uploading, setUploading] = useState(false)
  const [uploadProgress, setUploadProgress] = useState(0)
  const dispatch = useDispatch()

  const handleFileDrop = file => {
    const fileName = file.name
    if (fileName.match('^([0-2][0-9]|(3)[0-1])(\\.)(((0)[0-9])|((1)[0-2]))(\\.)\\d{4}.*$')) {
      setDate(new Date(fileName.substring(6, 10), fileName.substring(3, 5) - 1, fileName.substring(0, 2)))
      setDateParsed(true)
    }
    setFile(file)
  }

  const resetState = () => {
    setFile(null)
    setDate(new Date())
    setDateParsed(false)
    setUploading(false)
    setUploadProgress(0)
  }

  const handleCloseWithReset = () => {
    resetState()
    handleClose()
  }

  const setUploadProgressWrapper = progress => {
    setUploadProgress(progress)
    if (progress === 100) {
      setTimeout(handleCloseWithReset, 550)
    }
  }

  const handleSubmit = () => {
    setUploading(true)
    dispatch(uploadDailyRegistry(date, file, setUploadProgressWrapper))
  }

  return (
    <Dialog open={isOpen} onClose={handleClose} maxWidth={'xs'}>
      <DialogTitle>Add new daily registry</DialogTitle>
      <DialogContent>
        <DialogContentText>
          To add new daily registry, please drag 'n' drop some files on area below, or click to select files
        </DialogContentText>
        <RegistryDropzone
          onDrop={handleFileDrop}
          file={file}
        />
        <DatePicker
          disabled={uploading}
          dateParsed={dateParsed}
          isFileDropped={!!file}
          date={date}
          handleDateChange={setDate}
        />
        <LinearProgressDeterminate
          disabled={!uploading}
          progress={uploadProgress}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose} color="secondary">
            Cancel
        </Button>
        <Button disabled={!file || uploading} onClick={handleSubmit} color="primary">
            Upload
        </Button>
      </DialogActions>
    </Dialog>
  )
}