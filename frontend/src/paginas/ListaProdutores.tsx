import { useEffect, useState } from 'react'
import type { ChangeEvent, MouseEvent } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import {
  IconButton,
  Menu,
  MenuItem,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TablePagination,
  TableRow,
  Tooltip,
  useMediaQuery,
} from '@mui/material'
import { cliente } from '../api/cliente.ts'
import { mensagemDoErro } from '../api/erro.ts'
import { Erro } from '../componentes/Estados.tsx'
import { Modal, ModalLista, ModalQuadro } from '../componentes/Modal.tsx'
import { Notify } from '../componentes/Notify.tsx'
import {
  ALTURA_CABECALHO,
  ALTURA_TABELA,
  LINHAS_VISIVEIS,
  linhaFixa,
} from '../layout.ts'
import type { Produtor } from '../tipos.ts'

const itemMenu = {
  fontSize: 14,
  color: '#4a5553',
  py: 1,
  '&:hover': { backgroundColor: '#f2f2f2' },
}

const celulaObs = { width: 48, minWidth: 48, px: '4px' }
const celulaMenu = { width: 40, minWidth: 40, px: '2px' }

function dia(iso: string) {
  return new Date(iso).toLocaleDateString('pt-BR')
}

function textoDe(produtor: Produtor) {
  return [
    produtor.codigo,
    produtor.nome,
    produtor.laboratorio.nome,
    produtor.propriedades.map((fazenda) => fazenda.nome).join(' '),
  ]
    .join(' ')
    .toLowerCase()
}

function IconeObs() {
  return (
    <svg width="20" height="20" viewBox="0 0 20 20" fill="none" aria-hidden="true">
      <path
        d="M7 20C6.73478 20 6.48043 19.8946 6.29289 19.7071C6.10536 19.5196 6 19.2652 6 19V16H2C1.46957 16 0.960859 15.7893 0.585786 15.4142C0.210714 15.0391 0 14.5304 0 14V2C0 0.89 0.9 0 2 0H18C18.5304 0 19.0391 0.210714 19.4142 0.585786C19.7893 0.960859 20 1.46957 20 2V14C20 14.5304 19.7893 15.0391 19.4142 15.4142C19.0391 15.7893 18.5304 16 18 16H11.9L8.2 19.71C8 19.9 7.75 20 7.5 20H7ZM8 14V17.08L11.08 14H18V2H2V14H8ZM4 5H16V7H4V5ZM4 9H13V11H4V9Z"
        fill="#828D8C"
      />
    </svg>
  )
}

function IconeOpcoes() {
  return (
    <svg width="4" height="16" viewBox="0 0 4 16" fill="none" aria-hidden="true">
      <path
        d="M2 12C2.53043 12 3.03914 12.2107 3.41421 12.5858C3.78929 12.9609 4 13.4696 4 14C4 14.5304 3.78929 15.0391 3.41421 15.4142C3.03914 15.7893 2.53043 16 2 16C1.46957 16 0.96086 15.7893 0.585787 15.4142C0.210714 15.0391 0 14.5304 0 14C0 13.4696 0.210714 12.9609 0.585787 12.5858C0.96086 12.2107 1.46957 12 2 12ZM2 6C2.53043 6 3.03914 6.21071 3.41421 6.58579C3.78929 6.96086 4 7.46957 4 8C4 8.53043 3.78929 9.03914 3.41421 9.41421C3.03914 9.78929 2.53043 10 2 10C1.46957 10 0.96086 9.78929 0.585787 9.41421C0.210714 9.03914 0 8.53043 0 8C0 7.46957 0.210714 6.96086 0.585787 6.58579C0.96086 6.21071 1.46957 6 2 6V6ZM2 0C2.53043 0 3.03914 0.210714 3.41421 0.585786C3.78929 0.960859 4 1.46957 4 2C4 2.53043 3.78929 3.03914 3.41421 3.41421C3.03914 3.78929 2.53043 4 2 4C1.46957 4 0.96086 3.78929 0.585787 3.41421C0.210714 3.03914 0 2.53043 0 2C0 1.46957 0.210714 0.960859 0.585787 0.585786C0.96086 0.210714 1.46957 0 2 0Z"
        fill="#828D8C"
      />
    </svg>
  )
}

function BotaoObs({ aoAbrir }: { aoAbrir: () => void }) {
  return (
    <Tooltip title="Descrição" placement="top">
      <IconButton aria-label="Observação" size="small" sx={{ p: 0.5 }} onClick={aoAbrir}>
        <IconeObs />
      </IconButton>
    </Tooltip>
  )
}

function IconeBusca() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <circle cx="11" cy="11" r="6.25" stroke="currentColor" strokeWidth="1.8" />
      <path d="M16 16.5L20 20.5" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" />
    </svg>
  )
}

function PropriedadesDoProdutor({ produtor, aoAbrir }: { produtor: Produtor; aoAbrir: () => void }) {
  if (produtor.propriedades.length === 0) return <span>—</span>
  if (produtor.propriedades.length === 1) return <span>{produtor.propriedades[0].nome}</span>
  return (
    <button type="button" onClick={aoAbrir} className="text-[#00735D] underline">
      ({produtor.propriedades.length}) propriedades
    </button>
  )
}

export function ListaProdutores() {
  const navegar = useNavigate()
  const local = useLocation()
  const compacto = useMediaQuery('(max-width:599px)')
  const [produtores, setProdutores] = useState<Produtor[] | null>(null)
  const [erro, setErro] = useState<string | null>(null)
  const [sucesso, setSucesso] = useState<string | null>(null)
  const [escolhido, setEscolhido] = useState<Produtor | null>(null)
  const [fazendas, setFazendas] = useState<Produtor | null>(null)
  const [observacao, setObservacao] = useState<Produtor | null>(null)
  const [ancora, setAncora] = useState<HTMLElement | null>(null)
  const [alvo, setAlvo] = useState<Produtor | null>(null)
  const [termo, setTermo] = useState('')
  const [buscaAberta, setBuscaAberta] = useState(false)
  const [pagina, setPagina] = useState(0)
  const [porPagina, setPorPagina] = useState(10)
  const [tentativa, setTentativa] = useState(0)

  useEffect(() => {
    const state = local.state as { sucesso?: string } | null
    if (!state?.sucesso) return
    setSucesso(state.sucesso)
    navegar('.', { replace: true, state: {} })
  }, [local.state, navegar])

  useEffect(() => {
    let vivo = true
    cliente
      .get<Produtor[]>('/api/produtores')
      .then((resposta) => {
        if (vivo) {
          setProdutores(resposta.data)
          setErro(null)
        }
      })
      .catch((falha: unknown) => {
        if (vivo) setErro(mensagemDoErro(falha))
      })
    return () => {
      vivo = false
    }
  }, [tentativa])

  async function excluir() {
    if (!escolhido) return
    try {
      await cliente.delete(`/api/produtores/${escolhido.id}`)
      setEscolhido(null)
      setProdutores(null)
      setSucesso('Cadastro excluído com sucesso!')
      setTentativa((valor) => valor + 1)
    } catch (falha) {
      setEscolhido(null)
      setErro(mensagemDoErro(falha))
    }
  }

  function abrirMenu(evento: MouseEvent<HTMLButtonElement>, produtor: Produtor) {
    setAncora(evento.currentTarget)
    setAlvo(produtor)
  }

  function fecharMenu() {
    setAncora(null)
    setAlvo(null)
  }

  function editarAlvo() {
    if (!alvo) return
    const id = alvo.id
    fecharMenu()
    navegar(`/editar/${id}`)
  }

  function pedirExclusao() {
    if (!alvo) return
    setEscolhido(alvo)
    fecharMenu()
  }

  function alternarBusca() {
    setBuscaAberta((aberta) => !aberta)
    setTermo('')
    setPagina(0)
  }

  function mudarTamanho(evento: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
    setPorPagina(Number(evento.target.value))
    setPagina(0)
  }

  const lista = produtores ?? []
  const termoLimpo = termo.trim().toLowerCase()
  const filtrados = termoLimpo ? lista.filter((produtor) => textoDe(produtor).includes(termoLimpo)) : lista
  const ultima = Math.max(0, Math.ceil(filtrados.length / porPagina) - 1)
  const paginaSegura = Math.min(pagina, ultima)
  const visiveis = filtrados.slice(paginaSegura * porPagina, paginaSegura * porPagina + porPagina)
  const mensagemVazio = produtores !== null && visiveis.length === 0
  const linhasOcupadas = visiveis.length + (mensagemVazio ? 1 : 0)
  const vazias = Math.max(0, LINHAS_VISIVEIS - linhasOcupadas)

  return (
      <section className="flex min-h-0 flex-1 flex-col">
        <div className="mb-1 flex items-center gap-2 px-1 py-2">
          <span className="shrink-0 text-sm font-medium text-[#323E48]">Registros ({filtrados.length})</span>
          <span className="h-5 w-px shrink-0 bg-[#d5d5d5]" />
          <Link to="/novo" className="shrink-0 text-sm font-semibold tracking-wide text-[#00735D]">
            + ADICIONAR
          </Link>
          <div className="ml-auto flex min-w-0 items-center">
            {buscaAberta && (
              <input
                autoFocus
                value={termo}
                onChange={(evento) => {
                  setTermo(evento.target.value)
                  setPagina(0)
                }}
                placeholder="Buscar"
                aria-label="Buscar"
                className="mr-1 w-28 min-w-0 rounded border border-slate-300 px-2 py-1 text-sm sm:w-48"
              />
            )}
            <IconButton aria-label="Buscar" size="small" onClick={alternarBusca} sx={{ color: '#828D8C' }}>
              <IconeBusca />
            </IconButton>
          </div>
        </div>

        {erro && (
          <Erro
            mensagem={erro}
            aoTentar={() => {
              setErro(null)
              setProdutores(null)
              setTentativa((valor) => valor + 1)
            }}
          />
        )}

        {!erro && (
          <>
            <TableContainer
              sx={{
                height: ALTURA_TABELA,
                overflowX: 'auto',
                overflowY: visiveis.length > LINHAS_VISIVEIS ? 'auto' : 'hidden',
                WebkitOverflowScrolling: 'touch',
                scrollbarWidth: 'thin',
                scrollbarColor: '#c5cdcb transparent',
                '&::-webkit-scrollbar': { width: 4, height: 6 },
                '&::-webkit-scrollbar-track': { background: 'transparent' },
                '&::-webkit-scrollbar-thumb': {
                  backgroundColor: '#c5cdcb',
                  borderRadius: 4,
                },
              }}
            >
              <Table
                stickyHeader
                sx={{
                  minWidth: 920,
                  tableLayout: { xs: 'auto', md: 'fixed' },
                }}
              >
                <TableHead>
                  <TableRow sx={{ height: ALTURA_CABECALHO }}>
                    <TableCell>Código</TableCell>
                    <TableCell>Nome</TableCell>
                    <TableCell>Data Inicial</TableCell>
                    <TableCell>Data Final</TableCell>
                    <TableCell>Propriedade(s)</TableCell>
                    <TableCell>Laboratório</TableCell>
                    <TableCell align="center" sx={celulaObs}>Obs.</TableCell>
                    <TableCell align="center" sx={celulaMenu} />
                  </TableRow>
                </TableHead>
                <TableBody>
                  {visiveis.map((produtor) => (
                    <TableRow key={produtor.id} hover sx={linhaFixa}>
                      <TableCell>{produtor.codigo}</TableCell>
                      <TableCell>{produtor.nome}</TableCell>
                      <TableCell>{dia(produtor.dataInicial)}</TableCell>
                      <TableCell>{dia(produtor.dataFinal)}</TableCell>
                      <TableCell>
                        <PropriedadesDoProdutor produtor={produtor} aoAbrir={() => setFazendas(produtor)} />
                      </TableCell>
                      <TableCell>{produtor.laboratorio.nome}</TableCell>
                      <TableCell align="center" sx={celulaObs}>
                        <BotaoObs aoAbrir={() => setObservacao(produtor)} />
                      </TableCell>
                      <TableCell align="center" sx={celulaMenu}>
                        <Tooltip title="Opções" placement="top">
                          <IconButton
                            aria-label="Opções"
                            size="small"
                            sx={{ p: 0.5 }}
                            onClick={(evento) => abrirMenu(evento, produtor)}
                          >
                            <IconeOpcoes />
                          </IconButton>
                        </Tooltip>
                      </TableCell>
                    </TableRow>
                  ))}
                  {mensagemVazio && (
                    <TableRow sx={linhaFixa}>
                      <TableCell colSpan={8} sx={{ color: '#8d9896' }}>
                        {produtores.length === 0 ? 'Nenhum cadastro encontrado.' : 'Nenhum cadastro nesta busca.'}
                      </TableCell>
                    </TableRow>
                  )}
                  {Array.from({ length: vazias }, (_, indice) => (
                    <TableRow key={`vazio-${indice}`} sx={linhaFixa}>
                      <TableCell colSpan={8} />
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>

            <TablePagination
              component="div"
              count={filtrados.length}
              page={paginaSegura}
              onPageChange={(_, nova) => setPagina(nova)}
              rowsPerPage={porPagina}
              onRowsPerPageChange={mudarTamanho}
              rowsPerPageOptions={[10, 20, 50]}
              labelRowsPerPage={compacto ? '' : 'Registros por página'}
              labelDisplayedRows={({ from, to, count }) => `${from}-${to} de ${count}`}
              showFirstButton={!compacto}
              showLastButton={!compacto}
              getItemAriaLabel={(tipo) => {
                if (tipo === 'first') return 'Primeira página'
                if (tipo === 'last') return 'Última página'
                if (tipo === 'next') return 'Próxima página'
                return 'Página anterior'
              }}
              sx={{
                color: '#828D8C',
                borderTop: '1px solid #eee',
                overflow: 'hidden',
                '& .MuiTablePagination-spacer': { display: 'none' },
                '& .MuiTablePagination-toolbar': {
                  justifyContent: 'center',
                  minHeight: 48,
                  px: { xs: 0.5, sm: 2 },
                  gap: 0.5,
                },
                '& .MuiTablePagination-selectLabel, & .MuiTablePagination-displayedRows': {
                  fontSize: 13,
                  color: '#828D8C',
                },
                '& .MuiTablePagination-selectLabel': {
                  display: { xs: 'none', sm: 'block' },
                },
                '& .MuiTablePagination-actions': { ml: { xs: 0, sm: 2 } },
                '& .MuiIconButton-root': { color: '#828D8C' },
              }}
            />
          </>
        )}

        <Menu
          anchorEl={ancora}
          open={Boolean(ancora)}
          onClose={fecharMenu}
          anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
          transformOrigin={{ vertical: 'top', horizontal: 'right' }}
          slotProps={{
            paper: { sx: { minWidth: 140, mt: 0.5, borderRadius: '2px', boxShadow: '0 2px 8px rgba(0,0,0,0.15)' } },
          }}
        >
          <MenuItem onClick={editarAlvo} sx={itemMenu}>Editar</MenuItem>
          <MenuItem onClick={pedirExclusao} sx={itemMenu}>Excluir</MenuItem>
        </Menu>

        {fazendas && (
          <ModalLista
            titulo={`Propriedades (${fazendas.propriedades.length})`}
            itens={fazendas.propriedades.map((fazenda) => fazenda.nome)}
            aoFechar={() => setFazendas(null)}
          />
        )}

        {observacao && (
          <ModalQuadro titulo="Observações" aoFechar={() => setObservacao(null)}>
            <p>{observacao.observacoes?.trim() || 'Sem observação.'}</p>
          </ModalQuadro>
        )}

        {escolhido && (
          <Modal
            titulo="Excluir produtor"
            texto={`Excluir ${escolhido.nome}? A fazenda e o laboratório continuam.`}
            confirmar="Excluir"
            aoCancelar={() => setEscolhido(null)}
            aoConfirmar={() => {
              void excluir()
            }}
          />
        )}

        <Notify mensagem={sucesso} aoFechar={() => setSucesso(null)} />
      </section>
  )
}
