import TableContainer from '@material-ui/core/TableContainer'
import Table from '@material-ui/core/Table'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import { TableCell } from '@material-ui/core'
import TableBody from '@material-ui/core/TableBody'
import TablePagination from '@material-ui/core/TablePagination'
import Paper from '@material-ui/core/Paper'
import React, { useEffect, useState } from 'react'
import { LinearIndeterminate } from '../../../../components/LinearProgress/LinearProgress'
import { useDispatch, useSelector } from 'react-redux'
import { fetchDailyRegistry } from '../../../../store/registry/daily/operations'
import { DailyRegistryTableRow } from './DailyRegistryTableRow/DailyRegistryTableRow'

export const DailyRegistryTable = props => {
  const totalElements = useSelector(state => state.dailyRegistry.totalElements)
  const isLoading = useSelector(state => state.dailyRegistry.isLoading)
  const dailyRegistryList = useSelector(state => state.dailyRegistry.registryList)
  const { date } = props

  const [page, setPage] = useState(0)

  const dispatch = useDispatch()

  useEffect(() => dispatch(fetchDailyRegistry(date, page)), [date, page, dispatch])

  const handleChangePage = (event, newPage) => {
    setPage(newPage)
  }

  const progress = isLoading ? <LinearIndeterminate/> : null

  return (
    <Paper>
      <TableContainer>
        {progress}
        <Table stickyHeader aria-label="sticky table" size={'small'}>
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
            {dailyRegistryList.map(registry => <DailyRegistryTableRow registry={registry} date={date} page={page}/>)}
          </TableBody>
        </Table>
      </TableContainer>
      <TablePagination
        component="div"
        count={totalElements}
        rowsPerPage={10}
        rowsPerPageOptions={[10]}
        page={page}
        onChangePage={handleChangePage}
      />
    </Paper>)
}