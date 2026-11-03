import { createTheme } from '@mui/material'

export const tema = createTheme({
  palette: {
    primary: { main: '#00735D' },
    text: { primary: '#323E48', secondary: '#8d9896' },
  },
  typography: { fontFamily: 'inherit' },
  components: {
    MuiTableCell: {
      styleOverrides: {
        root: {
          fontSize: 14,
          color: '#5c6765',
          borderColor: '#ececec',
          padding: '11px 12px',
          whiteSpace: 'nowrap',
          overflow: 'hidden',
          textOverflow: 'ellipsis',
        },
        head: {
          color: '#8d9896',
          fontWeight: 400,
          backgroundColor: '#fff',
        },
      },
    },
    MuiTooltip: {
      styleOverrides: {
        tooltip: {
          backgroundColor: '#7a8684',
          fontSize: 13,
          fontWeight: 400,
          padding: '6px 10px',
          borderRadius: 2,
        },
      },
    },
    MuiInputLabel: {
      styleOverrides: {
        root: { color: '#8d9896' },
      },
    },
  },
})
