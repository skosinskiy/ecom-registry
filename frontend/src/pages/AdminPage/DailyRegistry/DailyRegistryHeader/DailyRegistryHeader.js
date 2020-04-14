import { DatePicker, MuiPickersUtilsProvider } from '@material-ui/pickers'
import DateFnsUtils from '@date-io/date-fns'
import Button from '@material-ui/core/Button'
import Grid from '@material-ui/core/Grid'
import React, { useState } from 'react'
import makeStyles from '@material-ui/core/styles/makeStyles'
import { AddNewModal } from '../AddNewModal/AddNewModal'

const useStyles = makeStyles((theme) => ({
  addNewButton: {
    marginBottom: theme.spacing(3)
  }
}))

export const DailyRegistryHeader = props => {
  const { date, setDate } = props
  const classes = useStyles()
  const [isModalOpen, setIsModalOpen] = useState(false)

  return (
    <Grid container justify={'space-between'}>
      <MuiPickersUtilsProvider utils={DateFnsUtils}>
        <DatePicker
          views={['year', 'month']}
          minDate={new Date('2020-01-01')}
          maxDate={new Date()}
          value={date}
          onChange={setDate}
        />
      </MuiPickersUtilsProvider>
      <Button
        className={classes.addNewButton}
        variant="outlined"
        color="secondary"
        onClick={() => setIsModalOpen(true)}
      >
          Add registry
      </Button>
      <AddNewModal isOpen={isModalOpen} date={date} page={0} handleClose={() => setIsModalOpen(false)}/>
    </Grid>
  )
}
