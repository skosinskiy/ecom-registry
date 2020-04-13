import {StatusBlock} from '../../../../components/StatusBlock/StatusBlock'
import React from 'react'
import useTheme from '@material-ui/core/styles/useTheme'
import {fade} from '@material-ui/core/styles/colorManipulator'

const getColorByStatus = (status, theme) => {
  switch (status) {
    case 'CREATED': return theme.palette.info.light
    case 'PARSING': return theme.palette.warning.light
    default : return theme.palette.success.light
  }
}

const getColors = color => {
  return {
    color: color,
    backgroundColor: fade(color, 0.1)
  }
}

export const DailyRegistryStatus = props => {
  const { status } = props
  const theme = useTheme()
  return <StatusBlock text={status} colors={getColors(getColorByStatus(status, theme))}/>
}