import TableRow from '@material-ui/core/TableRow'
import {TableCell} from '@material-ui/core'
import React, {useState} from 'react'
import Chip from '@material-ui/core/Chip'
import makeStyles from '@material-ui/core/styles/makeStyles'
import EditIconRounded from '@material-ui/icons/EditRounded'
import DeleteIconRounded from '@material-ui/icons/DeleteRounded'
import IconButton from '@material-ui/core/IconButton'
import SaveRoundedIcon from '@material-ui/icons/SaveRounded'
import ClearRoundedIcon from '@material-ui/icons/ClearRounded'
import TextField from '@material-ui/core/TextField'
import InputBase from '@material-ui/core/InputBase'

export const DailyRegistryParseCriteriaTableRow = props => {
  const { criteria, key } = props
  const { name, filterColumnName, filterValues } = criteria

  const [ isEditing, setIsEditing ] = useState(false)

  const useStyles = makeStyles((theme) => ({
    filterValue: {
      marginRight: theme.spacing(2),
      marginTop: theme.spacing(1)
    },
    input: {
      fontSize: '12px'
    }
  }))

  const handleEditClick = () => {
    setIsEditing(!isEditing)
  }

  const classes = useStyles()

  const criteriaName = isEditing
    ? <TextField
      InputProps={{
        classes: {
          input: classes.input
        }
      }}
      color={'primary'}
      defaultValue={name} />
    : <InputBase className={classes.input} readOnly defaultValue={name}/>

  const columnName = isEditing
    ? <TextField
      InputProps={{
        classes: {
          input: classes.input
        }
      }}
      color={'primary'}
      defaultValue={filterColumnName} />
    : <InputBase className={classes.input} readOnly defaultValue={filterColumnName}/>

  const chip = value => isEditing
    ? <Chip className={classes.filterValue} label={value} onDelete={() => console.log('a')}/>
    : <Chip className={classes.filterValue} label={value} />

  const filterValuesChips = filterValues.map(value => chip(value))
  if (isEditing) {
    filterValuesChips.push(
      <Chip
        className={classes.filterValue}
        clickable
        label={'+'}
      />)
  }

  const editButton =
    <IconButton onClick={handleEditClick}>
      {isEditing ? <ClearRoundedIcon/> : <EditIconRounded/> }
    </IconButton>

  const saveButton = isEditing
    ? <IconButton>
      <SaveRoundedIcon/>
    </IconButton>
    : null

  return (
    <TableRow hover key={key}>
      <TableCell>{criteriaName}</TableCell>
      <TableCell>{columnName}</TableCell>
      <TableCell>{filterValuesChips}</TableCell>
      <TableCell>
        {editButton}
        {saveButton}
        <IconButton>
          <DeleteIconRounded/>
        </IconButton>
      </TableCell>
    </TableRow>
  )
}