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
import ArchiveIcon from '@material-ui/icons/Archive'
import {saveFile} from '../../../helpers/dailyRegistry'
import {StatusBlock} from '../../../components/StatusBlock/StatusBlock'

const getStatusColor = status => {
  switch (status) {
    case 'CREATED':
      return {
        color: '#0F52BA',
        backgroundColor: 'rgba(87, 160, 211, 0.08)'
      }
    case 'PARSING':
      return {
        color: '#ff9800',
        backgroundColor: 'rgba(255, 152, 0, 0.08)'
      }
    case 'PARSED':
      return {
        color: '#4caf50',
        backgroundColor: 'rgba(76, 175, 80, 0.08)'
      }
  }
}

export const DailyRegistry = props => {
  const dailyRegistryList = useSelector(state => state.dailyRegistry.registryList)
  const isLoading = useSelector(state => state.dailyRegistry.isLoading)
  const dispatch = useDispatch()

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
    const {user, registryItem, createdDate, status} = registry
    const fileKey = registryItem.fileKey
    const registryDate = registry.registryDate
    const extension = fileKey.substring(fileKey.lastIndexOf('.') + 1)
    return (
      <TableRow key={registry.id}>
        <TableCell>{registryDate}</TableCell>
        <TableCell>{`${user.firstName} ${user.lastName}`}</TableCell>
        <TableCell>{`${convertBytesToMegaBytes(registryItem.size)} MB`}</TableCell>
        <TableCell>{extension}</TableCell>
        <TableCell>{getDateTimeString(createdDate)}</TableCell>
        <TableCell>
          <StatusBlock text={status} colors={getStatusColor(status)}/>
        </TableCell>
        <TableCell>
          <IconButton size="medium" onClick={() => dispatch(parseDailyRegistry(registry.id))}>
            <ArchiveIcon/>
          </IconButton>
          <IconButton size="medium" onClick={() =>
            saveFile(`/api/registry/daily/${registry.id}`, `${registryDate}.${extension}`)}>
            <ArrowDownwardIcon/>
          </IconButton>
          <IconButton size="medium" onClick={() => dispatch(deleteDailyRegistry(registry.id))}>
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
        <Button variant="outlined" color="primary" onClick={handleModal}>
            Add registry
        </Button>
      </Grid>
      <Paper>
        <AddNewModal isOpen={modalOpen} handleClose={handleModal}/>
        <Table>
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