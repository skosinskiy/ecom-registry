import React, {useEffect, useState} from 'react'
import Table from '@material-ui/core/Table'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import {TableCell} from '@material-ui/core'
import Paper from '@material-ui/core/Paper'
import {useDispatch, useSelector} from 'react-redux'
import {deleteDailyRegistry, fetchDailyRegistry} from '../../../store/registry/daily/operations'
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
    const {user, fileItem, createdDate} = registry
    const fileKey = fileItem.fileKey
    const registryDate = registry.registryDate;
    const extension = fileKey.substring(fileKey.lastIndexOf('.') + 1);
    return (
      <TableRow key={registry.id}>
        <TableCell>{registryDate}</TableCell>
        <TableCell>{`${user.firstName} ${user.lastName}`}</TableCell>
        <TableCell>{`${convertBytesToMegaBytes(fileItem.size)} MB`}</TableCell>
        <TableCell>{extension}</TableCell>
        <TableCell>{getDateTimeString(createdDate)}</TableCell>
        <TableCell>
          <IconButton size="medium" onClick={() =>
            saveFile(`/api/registry/daily/${registry.id}`, `${registryDate}.${extension}`)}>
            <ArrowDownwardIcon fontSize="medium" />
          </IconButton>
          <IconButton size="medium" onClick={() => dispatch(deleteDailyRegistry(registry.id))}>
            <DeleteIcon fontSize="inherit" />
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
              <TableCell/>
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