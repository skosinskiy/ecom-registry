import DateFnsUtils from '@date-io/date-fns'
import {KeyboardDatePicker, MuiPickersUtilsProvider} from '@material-ui/pickers'
import React from 'react'
import Grid from '@material-ui/core/Grid'
import Alert from '@material-ui/lab/Alert'

export const DatePicker = props => {
  const {isFileDropped, date, handleDateChange} = props

  if (isFileDropped) {
    return (
      <Grid container
        direction={'column'}>
        <Alert severity="warning">Please check registry date</Alert>
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