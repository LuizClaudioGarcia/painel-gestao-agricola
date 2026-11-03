import { DatePicker } from '@mui/x-date-pickers/DatePicker'
import type { Dayjs } from 'dayjs'
import dayjs from 'dayjs'

function IconeCalendario() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <rect x="3.5" y="5.5" width="17" height="15" rx="1.5" stroke="currentColor" strokeWidth="1.6" />
      <path d="M8 3.5v4M16 3.5v4M3.5 10h17" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" />
    </svg>
  )
}

type Props = {
  rotulo: string
  valor: string
  aoMudar: (valor: string) => void
}

export function CampoData({ rotulo, valor, aoMudar }: Props) {
  return (
    <DatePicker
      label={rotulo}
      format="DD/MM/YYYY"
      value={valor ? dayjs(valor) : null}
      onChange={(data: Dayjs | null) => {
        aoMudar(data && data.isValid() ? data.format('YYYY-MM-DD') : '')
      }}
      slots={{ openPickerIcon: IconeCalendario }}
      slotProps={{
        textField: {
          variant: 'standard',
          fullWidth: true,
          required: true,
          sx: {
            minHeight: 48,
            '& .MuiInput-root': { minHeight: 32, pr: 0.5 },
            '& .MuiInput-input': {
              height: 24,
              boxSizing: 'border-box',
              fontVariantNumeric: 'tabular-nums',
              letterSpacing: 0.2,
            },
          },
          slotProps: { inputLabel: { shrink: true } },
        },
        openPickerButton: { sx: { color: '#828D8C', p: 0.5 } },
        desktopPaper: {
          sx: {
            mt: 1,
            borderRadius: '4px',
            boxShadow: '0 8px 24px rgba(0,0,0,0.16)',
          },
        },
        day: {
          sx: {
            fontSize: 13,
            '&.Mui-selected': {
              backgroundColor: '#00735D',
              '&:hover, &:focus': { backgroundColor: '#005c4a' },
            },
            '&.MuiPickersDay-today:not(.Mui-selected)': {
              border: '1px solid #323E48',
            },
          },
        },
      }}
    />
  )
}
