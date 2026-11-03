import { NavLink, useLocation, useNavigate } from 'react-router-dom'

function Chevron() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d="M15 6L9 12l6 6" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  )
}

const itemNav = ({ isActive }: { isActive: boolean }) =>
  isActive ? 'font-semibold text-white' : 'text-white/70 hover:text-white'

export function Barra() {
  const { pathname } = useLocation()
  const navegar = useNavigate()
  const noPainel = pathname.startsWith('/painel')
  const noFormulario = pathname.startsWith('/novo') || pathname.startsWith('/editar')
  const podeVoltar = pathname !== '/'

  let atual = ''
  if (noPainel) atual = 'Consultas'
  else if (pathname.startsWith('/novo')) atual = 'Novo Cadastro'
  else if (pathname.startsWith('/editar')) atual = 'Editar Cadastro'

  return (
    <header className="flex items-center gap-1 bg-[#00735D] px-2 py-2.5 text-white">
      <span className="inline-flex w-7 shrink-0 items-center justify-center">
        {podeVoltar && (
          <button
            type="button"
            aria-label="Voltar"
            className="rounded p-1 hover:bg-white/10"
            onClick={() => navegar('/')}
          >
            <Chevron />
          </button>
        )}
      </span>
      <h1 className="min-w-0 flex-1 truncate text-[17px]">
        {atual ? (
          <>
            <span className="hidden font-normal text-white/70 sm:inline">Teste Front-End / </span>
            <span className="font-semibold">{atual}</span>
          </>
        ) : (
          <span className="font-semibold">Teste Front-End</span>
        )}
      </h1>
      {noFormulario ? (
        <button
          type="submit"
          form="form-produtor"
          className="ml-auto px-3 text-sm font-medium tracking-wider text-white"
        >
          SALVAR
        </button>
      ) : (
        <nav className="ml-auto flex items-center gap-5 pr-2 text-sm" aria-label="Seções">
          <NavLink to="/" end className={itemNav}>
            Registros
          </NavLink>
          <NavLink to="/painel" className={itemNav}>
            Consultas
          </NavLink>
        </nav>
      )}
    </header>
  )
}
