import { ThemeProvider } from '@mui/material'
import { LocalizationProvider } from '@mui/x-date-pickers'
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs'
import { BrowserRouter, NavLink, Route, Routes } from 'react-router-dom'
import { Barra } from './componentes/Barra.tsx'
import { Logo } from './componentes/Logo.tsx'
import { FormProdutor } from './paginas/FormProdutor.tsx'
import { ListaProdutores } from './paginas/ListaProdutores.tsx'
import { Painel } from './paginas/Painel.tsx'
import { ALTURA_QUADRO } from './layout.ts'
import { tema } from './tema.ts'
import 'dayjs/locale/pt-br'

export default function App() {
  return (
    <ThemeProvider theme={tema}>
      <LocalizationProvider
        dateAdapter={AdapterDayjs}
        adapterLocale="pt-br"
        localeText={{
          fieldDayPlaceholder: () => 'dd',
          fieldMonthPlaceholder: () => 'mm',
          fieldYearPlaceholder: () => 'aaaa',
        }}
      >
      <BrowserRouter>
        <div className="mx-auto min-h-screen w-full max-w-[100rem] px-2 pb-6 sm:px-6 sm:pb-8 lg:px-10">
          <div className="flex justify-center py-4 sm:py-5">
            <NavLink to="/" aria-label="Início">
              <Logo />
            </NavLink>
          </div>
          <div
            className="flex flex-col overflow-hidden bg-white shadow-[0_1px_4px_rgba(0,0,0,0.08)]"
            style={{ minHeight: ALTURA_QUADRO }}
          >
            <Barra />
            <main className="flex min-h-0 min-w-0 flex-1 flex-col px-2 pb-4 pt-3 sm:px-6 sm:pb-6 sm:pt-4">
              <Routes>
                <Route path="/" element={<ListaProdutores />} />
                <Route path="/novo" element={<FormProdutor />} />
                <Route path="/editar/:id" element={<FormProdutor />} />
                <Route path="/painel" element={<Painel />} />
              </Routes>
            </main>
          </div>
        </div>
      </BrowserRouter>
      </LocalizationProvider>
    </ThemeProvider>
  )
}
