import React, {Component} from 'react'
import Table from '@material-ui/core/Table'
import TableHead from '@material-ui/core/TableHead'
import TableRow from '@material-ui/core/TableRow'
import {TableCell} from '@material-ui/core'
import Paper from '@material-ui/core/Paper'

class FileManager extends Component {
  render () {
    return (
      <Paper>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Name</TableCell>
              <TableCell>Type</TableCell>
              <TableCell>Owner</TableCell>
              <TableCell>Size</TableCell>
              <TableCell>Created</TableCell>
            </TableRow>
          </TableHead>
        </Table>
      </Paper>
    )
  }
}

export default FileManager