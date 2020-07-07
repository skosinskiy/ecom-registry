import React, {useEffect} from 'react'
import Paper from '@material-ui/core/Paper'
import TableContainer from '@material-ui/core/TableContainer'
import {LinearIndeterminate} from '../../../components/LinearProgress/LinearProgress'
import Table from '@material-ui/core/Table'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import {TableCell} from '@material-ui/core'
import TableBody from '@material-ui/core/TableBody'
import {useDispatch, useSelector} from 'react-redux'
import {DailyRegistryParseCriteriaTableRow} from './DailyRegistryParseCriteriaTableRow/DailyRegistryParseCriteriaTableRow'
import {fetchDailyRegistryParseCriteria, updatePage} from '../../../store/criteria/daily/operations'
import TablePagination from '@material-ui/core/TablePagination'

export const DailyRegistryParseCriteria = () => {
  const isLoading = useSelector(state => state.dailyRegistryParseCriteria.isLoading)
  const totalElements = useSelector(state => state.dailyRegistryParseCriteria.totalElements)
  const criteriaList = useSelector(state => state.dailyRegistryParseCriteria.criteriaList)
  const page = useSelector(state => state.dailyRegistryParseCriteria.page)

  const dispatch = useDispatch()

  useEffect(() => dispatch(fetchDailyRegistryParseCriteria(page)), [page, dispatch])

  const handleChangePage = (event, newPage) => {
    dispatch(updatePage(newPage))
  }

  return (
    <Paper>
      <TableContainer>
        <LinearIndeterminate color={'secondary'} disabled={!isLoading}/>
        <Table stickyHeader aria-label="sticky table" size={'small'}>
          <colgroup>
            <col style={{ width: '20%' }}/>
            <col style={{ width: '20%' }}/>
            <col style={{ width: '60%' }}/>
          </colgroup>
          <TableHead>
            <TableRow>
              <TableCell>Criteria name</TableCell>
              <TableCell>Column name</TableCell>
              <TableCell>Filter values</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {criteriaList.map((criteria, i) =>
              <DailyRegistryParseCriteriaTableRow
                key={i}
                criteria={criteria}
                page={page}
                decreasePage={i === 0 && criteriaList.length === 1 && page > 0}
              />
            )}
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