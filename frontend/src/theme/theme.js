import {createMuiTheme} from '@material-ui/core'

export const theme = createMuiTheme({
  palette: {
    type: 'dark',
    primary: {500: '#388e3c'},
    secondary: {A400: '#f57c00'}
  },
  overrides: {
    MuiTableBody: {
      root: {
        fontSize: 12
      }
    },
    MuiTableCell: {
      root: {
        fontSize: 'inherit'
      }
    }
  }
})