import React, {useEffect, useState} from 'react'
import Table from '@material-ui/core/Table'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import {TableCell} from '@material-ui/core'
import Paper from '@material-ui/core/Paper'
import {useDispatch, useSelector} from 'react-redux'
import {deleteDailyRegistry, fetchDailyRegistry, parseDailyRegistry} from '../../../store/registry/daily/operations'
import Preloader from '../../../components/Preloader/Preloader'
import {getDateTimeString} from '../../../utils/dateUtils'
import TableBody from '@material-ui/core/TableBody'
import {convertBytesToMegaBytes} from '../../../utils/sizeConverter'
import Button from '@material-ui/core/Button'
import {AddNewModal} from './AddNewModal/AddNewModal'
import Grid from '@material-ui/core/Grid'
import IconButton from '@material-ui/core/IconButton'
import DeleteIcon from '@material-ui/icons/Delete'
import ArrowDownwardIcon from '@material-ui/icons/ArrowDownward'
import {saveFile} from '../../../helpers/dailyRegistry'
import {makeStyles} from '@material-ui/core/styles'
import {ParseButton} from './ParseButton/ParseButton'
import {DailyRegistryStatus} from './DailyRegistryStatus/DailyRegistryStatus'

const useStyles = makeStyles((theme) => ({
  addNewButton: {
    marginBottom: '30px'
  },
  actionCell: {
    marginLeft: theme.spacing(2)
  }
}))

export const DailyRegistry = () => {
  const dailyRegistryList = useSelector(state => state.dailyRegistry.registryList)
  const isLoading = useSelector(state => state.dailyRegistry.isLoading)
  const dispatch = useDispatch()
  const classes = useStyles()

  const [modalOpen, setModalOpen] = useState(false)

  const handleModal = () => setModalOpen(!modalOpen)

  useEffect(() => {
    if (dailyRegistryList === null) {
      dispatch(fetchDailyRegistry())
    }
  }, [dailyRegistryList, dispatch])

  if (isLoading) {
    return <Preloader/>
  }

  const registryItems = dailyRegistryList === null ? null : dailyRegistryList.map(registry => {
    const {user, registryItem, createdDate, status, parsedRegistryItem} = registry
    const date = registry.registryDate
    const registryItemExtension = registryItem.extension

    return (
      <TableRow key={registry.id}>
        <TableCell>{date}</TableCell>
        <TableCell>{`${user.firstName} ${user.lastName}`}</TableCell>
        <TableCell>{`${convertBytesToMegaBytes(registryItem.size)} MB`}</TableCell>
        <TableCell>{registryItemExtension}</TableCell>
        <TableCell>{getDateTimeString(createdDate)}</TableCell>
        <TableCell><DailyRegistryStatus status={status}/></TableCell>
        <TableCell>
          <ParseButton
            status={status}
            parseHandler={() => dispatch(parseDailyRegistry(registry.id))}
            downloadHandler={() => saveFile(`/api/files/binary/${parsedRegistryItem.id}`, `${date}.${parsedRegistryItem.extension}`)}
          />
          <IconButton
            className={classes.actionCell}
            size="small"
            onClick={() => saveFile(`/api/files/binary/${registryItem.id}`, `${date}.${registryItemExtension}`)}>
            <ArrowDownwardIcon/>
          </IconButton>
          <IconButton
            className={classes.actionCell}
            disabled={status === 'PARSING'}
            size="small"
            onClick={() => dispatch(deleteDailyRegistry(registry.id))}>
            <DeleteIcon/>
          </IconButton>
        </TableCell>
      </TableRow>
    )
  }
  )

  return (
    <div>
      <Grid container justify={'flex-end'}>
        <Button className={classes.addNewButton} variant="outlined" color="secondary" onClick={handleModal}>
            Add registry
        </Button>
      </Grid>
      <Paper>
        <AddNewModal isOpen={modalOpen} handleClose={handleModal}/>
        <Table size={'small'}>
          <TableHead>
            <TableRow>
              <TableCell>Registry date</TableCell>
              <TableCell>Owner</TableCell>
              <TableCell>Size</TableCell>
              <TableCell>Extension</TableCell>
              <TableCell>Created</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {registryItems}
          </TableBody>
        </Table>
      </Paper>
    </div>
  )
}