import IconButton from '@material-ui/core/IconButton'
import FolderIcon from '@material-ui/icons/Folder'
import React from 'react'
import ZipIcon from '../../../../icons/digital.svg'
import SvgIcon from '@material-ui/core/SvgIcon'

export const ParseButton = props => {
  const {status, parseHandler, downloadHandler} = props

  if (status === 'CREATED') {
    return (
      <IconButton size={'small'} onClick={parseHandler}>
        <SvgIcon component={ZipIcon}/>
      </IconButton>
    )
  }
  if (status === 'PARSED') {
    return (
      <IconButton
        size={'small'}
        onClick={downloadHandler}>
        <FolderIcon/>
      </IconButton>
    )
  }
  return (
    <IconButton disabled size={'small'}>
      <FolderIcon />
    </IconButton>
  )
}