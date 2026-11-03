import { useEffect, useState } from 'react'
import type { FormEvent } from 'react'
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material'
import { cliente } from '../api/cliente.ts'
import { mensagemDoErro } from '../api/erro.ts'
import { Carregando, Erro } from '../componentes/Estados.tsx'
import {
  ALTURA_CABECALHO,
  ALTURA_TABELA,
  LINHAS_VISIVEIS,
  linhaFixa,
} from '../layout.ts'
import type { LinhaPainel } from '../tipos.ts'

const campo = 'min-w-0 max-w-full rounded border border-slate-300 px-2 py-1'

export function Painel() {
  const [linhas, setLinhas] = useState<LinhaPainel[] | null>(null)
  const [erro, setErro] = useState<string | null>(null)
  const [quantidadeMinima, setQuantidadeMinima] = useState('1')
  const [dataInicialDe, setDataInicialDe] = useState('')
  const [dataInicialAte, setDataInicialAte] = useState('')
  const [dataFinalDe, setDataFinalDe] = useState('')
  const [dataFinalAte, setDataFinalAte] = useState('')
  const [observacoes, setObservacoes] = useState('')
  const [tentativa, setTentativa] = useState(0)

  function params() {
    const consulta: Record<string, string> = { quantidadeMinima: quantidadeMinima || '1' }
    if (dataInicialDe) consulta.dataInicialDe = `${dataInicialDe}T00:00:00Z`
    if (dataInicialAte) consulta.dataInicialAte = `${dataInicialAte}T23:59:59Z`
    if (dataFinalDe) consulta.dataFinalDe = `${dataFinalDe}T00:00:00Z`
    if (dataFinalAte) consulta.dataFinalAte = `${dataFinalAte}T23:59:59Z`
    if (observacoes.trim()) consulta.observacoes = observacoes.trim()
    return consulta
  }

  useEffect(() => {
    let vivo = true
    cliente
      .get<LinhaPainel[]>('/api/laboratorios/analitico', { params: { quantidadeMinima: '1' } })
      .then((resposta) => {
        if (vivo) {
          setLinhas(resposta.data)
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

  function aplicar(evento: FormEvent) {
    evento.preventDefault()
    if (Number(quantidadeMinima) < 1) {
      setErro('Quantidade mínima: menor que 1')
      setLinhas([])
      return
    }
    setLinhas(null)
    setErro(null)
    cliente
      .get<LinhaPainel[]>('/api/laboratorios/analitico', { params: params() })
      .then((resposta) => setLinhas(resposta.data))
      .catch((falha: unknown) => setErro(mensagemDoErro(falha)))
  }

  const dados = linhas ?? []
  const mensagemVazio = linhas !== null && dados.length === 0
  const linhasOcupadas = dados.length + (mensagemVazio ? 1 : 0)
  const vazias = Math.max(0, LINHAS_VISIVEIS - linhasOcupadas)

  return (
    <section className="flex min-h-0 flex-1 flex-col">
      <form onSubmit={aplicar} className="mb-1 flex flex-wrap items-center gap-2 px-1 py-2 text-sm">
        <input className={campo} type="number" min="1" value={quantidadeMinima} onChange={(e) => setQuantidadeMinima(e.target.value)} title="Quantidade mínima" />
        <input className={campo} type="date" value={dataInicialDe} onChange={(e) => setDataInicialDe(e.target.value)} title="Data inicial de" />
        <input className={campo} type="date" value={dataInicialAte} onChange={(e) => setDataInicialAte(e.target.value)} title="Data inicial até" />
        <input className={campo} type="date" value={dataFinalDe} onChange={(e) => setDataFinalDe(e.target.value)} title="Data final de" />
        <input className={campo} type="date" value={dataFinalAte} onChange={(e) => setDataFinalAte(e.target.value)} title="Data final até" />
        <input className={campo} placeholder="Observações" value={observacoes} onChange={(e) => setObservacoes(e.target.value)} />
        <button type="submit" className="rounded bg-[#00735D] px-3 py-1 text-white">Filtrar</button>
      </form>

      {erro && (
        <Erro
          mensagem={erro}
          aoTentar={() => {
            setErro(null)
            setLinhas(null)
            setTentativa((valor) => valor + 1)
          }}
        />
      )}
      {!erro && linhas === null && <Carregando />}

      {!erro && linhas !== null && (
        <TableContainer
          sx={{
            height: ALTURA_TABELA,
            overflowX: 'auto',
            overflowY: dados.length > LINHAS_VISIVEIS ? 'auto' : 'hidden',
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
          <Table stickyHeader sx={{ tableLayout: 'fixed' }}>
            <TableHead>
              <TableRow sx={{ height: ALTURA_CABECALHO }}>
                <TableCell>Código</TableCell>
                <TableCell>Nome</TableCell>
                <TableCell>Produtores</TableCell>
                <TableCell>Valor</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {dados.map((linha) => (
                <TableRow key={linha.codigo} hover sx={linhaFixa}>
                  <TableCell>{linha.codigo}</TableCell>
                  <TableCell>{linha.nome}</TableCell>
                  <TableCell>{linha.quantidadeProdutores}</TableCell>
                  <TableCell>
                    {linha.valor.toLocaleString('pt-BR', { minimumFractionDigits: 2 })}
                  </TableCell>
                </TableRow>
              ))}
              {mensagemVazio && (
                <TableRow sx={linhaFixa}>
                  <TableCell colSpan={4} sx={{ color: '#8d9896' }}>
                    Nenhum laboratório com esses filtros.
                  </TableCell>
                </TableRow>
              )}
              {Array.from({ length: vazias }, (_, indice) => (
                <TableRow key={`vazio-${indice}`} sx={linhaFixa}>
                  <TableCell colSpan={4} />
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
    </section>
  )
}
