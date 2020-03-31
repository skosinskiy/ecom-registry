import React, {useState} from 'react'
import DialogTitle from '@material-ui/core/DialogTitle'
import DialogContent from '@material-ui/core/DialogContent'
import DialogContentText from '@material-ui/core/DialogContentText'
import {ReactDropzone} from '../../../../components/Dropzone/Dropzone'
import DialogActions from '@material-ui/core/DialogActions'
import Button from '@material-ui/core/Button'
import Dialog from '@material-ui/core/Dialog'
import {DatePicker} from './DatePicker/DatePicker'
import {useDispatch} from 'react-redux'
import {uploadDailyRegistry} from '../../../../store/registry/daily/operations'

export const AddNewModal = props => {
  const {isOpen, handleClose} = props
  const [isFileDropped, setIsFileDropped] = useState(false)
  const [file, setFile] = useState(null)
  const [date, setDate] = useState(new Date())
  const dispatch = useDispatch()

  const handleFileDrop = files => {
    setIsFileDropped(!isFileDropped)
    const fileName = files[0].name
    const date = fileName.match('^([0-2][0-9]|(3)[0-1])(\\.)(((0)[0-9])|((1)[0-2]))(\\.)\\d{4}.*$')
      ? new Date(fileName.substring(6, 10), fileName.substring(3, 5) - 1, fileName.substring(0, 2))
      : new Date()
    setDate(date)
    setFile(files[0])
  }
  const handleDateChange = date => setDate(date)
  const handleSubmit = () => {
    handleClose()
    dispatch(uploadDailyRegistry(date, file))
  }

  return (
    <Dialog open={isOpen} onClose={handleClose} maxWidth={'xs'}>
      <DialogTitle>Add new daily registry</DialogTitle>
      <DialogContent>
        <DialogContentText>
          To add new daily registry, please drag 'n' drop some files on area below, or click to select files
        </DialogContentText>
        <ReactDropzone onDrop={handleFileDrop}/>
        <DatePicker isFileDropped={isFileDropped} date={date} handleDateChange={handleDateChange}/>
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose} color="primary">
            Cancel
        </Button>
        <Button onClick={handleSubmit} color="primary">
            Add
        </Button>
      </DialogActions>
    </Dialog>
  )
}