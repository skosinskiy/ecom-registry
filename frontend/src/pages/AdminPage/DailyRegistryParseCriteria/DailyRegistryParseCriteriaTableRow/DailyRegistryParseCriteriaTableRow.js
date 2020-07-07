import TableRow from '@material-ui/core/TableRow'
import {TableCell} from '@material-ui/core'
import React, {useState} from 'react'
import Chip from '@material-ui/core/Chip'
import makeStyles from '@material-ui/core/styles/makeStyles'

export const DailyRegistryParseCriteriaTableRow = props => {
  const { criteria, page, decreasePage, key } = props
  const { name, filterColumnName, filterValues } = criteria

  const { isEditing, setIsEditing } = useState(false)

  const useStyles = makeStyles((theme) => ({
    filterValue: {
      marginRight: theme.spacing(2)
    }
  }))

  const classes = useStyles()

  return (
    <TableRow hover key={key}>
      <TableCell>{name}</TableCell>
      <TableCell>{filterColumnName}</TableCell>
      <TableCell>{filterValues.map(value => <Chip className={classes.filterValue} label={value}/>
      )}</TableCell>
    </TableRow>
  )
}