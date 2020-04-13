import {createMuiTheme} from '@material-ui/core'
import green from '@material-ui/core/colors/green'
import orange from '@material-ui/core/colors/orange'

export const theme = createMuiTheme({
  palette: {
    type: 'dark',
    primary: {
      light: green[300],
      main: green[700],
      dark: green[800]
    },
    secondary: {
      light: orange[300],
      main: orange[700],
      dark: orange[800]
    }
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