import DateFnsUtils from '@date-io/date-fns'
import {KeyboardDatePicker, MuiPickersUtilsProvider} from '@material-ui/pickers'
import React from 'react'
import Grid from '@material-ui/core/Grid'
import Alert from '@material-ui/lab/Alert'

export const DatePicker = props => {
  const {isFileDropped, date, handleDateChange, dateParsed, disabled} = props

  const alert = dateParsed
    ? <Alert severity="success">Registry date successfully parsed from file name</Alert>
    : <Alert severity="error">Please check registry date</Alert>

  if (disabled) {
    return null
  }

  if (isFileDropped) {
    return (
      <Grid container
        direction={'column'}>
        {alert}
        <MuiPickersUtilsProvider utils={DateFnsUtils}>
          <KeyboardDatePicker
            disableToolbar
            variant="inline"
            format="dd.MM.yyyy"
            margin="normal"
            value={date}
            onChange={handleDateChange}
            KeyboardButtonProps={{
              'aria-label': 'change date'
            }}
          />
        </MuiPickersUtilsProvider>
      </Grid>
    )
  }
  return null
}