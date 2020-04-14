import TableRow from '@material-ui/core/TableRow'
import { TableCell } from '@material-ui/core'
import { convertBytesToMegaBytes } from '../../../../../utils/sizeConverter'
import ExcelIcon from '../../../../../icons/excel.svg'
import { getDateTimeString } from '../../../../../utils/dateUtils'
import { DailyRegistryStatus } from '../../DailyRegistryStatus/DailyRegistryStatus'
import { ParseButton } from '../../ParseButton/ParseButton'
import { deleteDailyRegistry, parseDailyRegistry } from '../../../../../store/registry/daily/operations'
import { saveFile } from '../../../../../helpers/dailyRegistry'
import IconButton from '@material-ui/core/IconButton'
import classNames from 'classnames'
import CloudDownloadRoundedIcon from '@material-ui/icons/CloudDownloadRounded'
import DeleteIconRounded from '@material-ui/icons/DeleteRounded'
import React from 'react'
import makeStyles from '@material-ui/core/styles/makeStyles'
import { useDispatch } from 'react-redux'

const useStyles = makeStyles((theme) => ({
  actionCell: {
    marginLeft: theme.spacing(2)
  },
  icon: {
    height: '20px',
    width: '20px',
    marginRight: theme.spacing(2)
  },
  extensionWrapper: {
    display: 'flex',
    alignItems: 'center'
  },
  downloadButton: {
    '&:hover': {
      color: theme.palette.info.light
    }
  },
  deleteButton: {
    '&:hover': {
      color: theme.palette.error.main
    }
  }
}))

export const DailyRegistryTableRow = props => {
  const { registry, date, page } = props
  const { id, user, registryItem, createdDate, registryDate, status, parsedRegistryItem } = registry

  const classes = useStyles()
  const dispatch = useDispatch()

  return (
    <TableRow key={id}>
      <TableCell>{registryDate}</TableCell>
      <TableCell>{`${user.firstName} ${user.lastName}`}</TableCell>
      <TableCell>{`${convertBytesToMegaBytes(registryItem.size)} MB`}</TableCell>
      <TableCell>
        <div className={classes.extensionWrapper}>
          <img className={classes.icon} src={ExcelIcon} alt={''}/>
          {registryItem.extension}
        </div>
      </TableCell>
      <TableCell>{getDateTimeString(createdDate)}</TableCell>
      <TableCell><DailyRegistryStatus status={status}/></TableCell>
      <TableCell>
        <ParseButton
          status={status}
          parseHandler={() => dispatch(parseDailyRegistry(id))}
          downloadHandler={() => saveFile(`/api/files/binary/${parsedRegistryItem.id}`, `${registryDate}.${parsedRegistryItem.extension}`)}
        />
        <IconButton
          className={classNames(classes.actionCell, classes.downloadButton)}
          size="small"
          onClick={() => saveFile(`/api/files/binary/${registryItem.id}`, `${registryDate}.${registryItem.extension}`)}>
          <CloudDownloadRoundedIcon/>
        </IconButton>
        <IconButton
          className={classNames(classes.actionCell, classes.deleteButton)}
          disabled={status === 'PARSING'}
          size="small"
          onClick={() => dispatch(deleteDailyRegistry(id, date, page))}>
          <DeleteIconRounded/>
        </IconButton>
      </TableCell>
    </TableRow>
  )
}