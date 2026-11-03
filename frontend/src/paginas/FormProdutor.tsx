import { useEffect, useState } from 'react'
import type { FormEvent } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { Autocomplete, Chip, Grid, TextField } from '@mui/material'
import { cliente } from '../api/cliente.ts'
import { mensagemDoErro } from '../api/erro.ts'
import { CampoData } from '../componentes/CampoData.tsx'
import { Carregando, Erro } from '../componentes/Estados.tsx'
import type { Cadastro, Produtor } from '../tipos.ts'

const LIMITE_OBS = 100

function dia(iso: string) {
  return iso.slice(0, 10)
}

function paraApi(data: string) {
  return `${data}T12:00:00Z`
}

const chipFazenda = {
  color: '#00735D',
  borderColor: '#00735D',
  backgroundColor: 'transparent',
  '& .MuiChip-deleteIcon': { color: '#00735D' },
}

export function FormProdutor() {
  const { id } = useParams()
  const navegar = useNavigate()
  const [laboratorios, setLaboratorios] = useState<Cadastro[] | null>(null)
  const [propriedades, setPropriedades] = useState<Cadastro[] | null>(null)
  const [erro, setErro] = useState<string | null>(null)
  const [aviso, setAviso] = useState<string | null>(null)
  const [salvando, setSalvando] = useState(false)
  const [tentativa, setTentativa] = useState(0)

  const [nome, setNome] = useState('')
  const [inscricaoFiscal, setInscricaoFiscal] = useState('')
  const [endereco, setEndereco] = useState('')
  const [dataInicial, setDataInicial] = useState('')
  const [dataFinal, setDataFinal] = useState('')
  const [comissaoProducao, setComissaoProducao] = useState('0')
  const [observacoes, setObservacoes] = useState('')
  const [laboratorio, setLaboratorio] = useState<Cadastro | null>(null)
  const [fazendas, setFazendas] = useState<Cadastro[]>([])

  useEffect(() => {
    let vivo = true
    Promise.all([
      cliente.get<Cadastro[]>('/api/laboratorios'),
      cliente.get<Cadastro[]>('/api/propriedades'),
      id ? cliente.get<Produtor>(`/api/produtores/${id}`) : Promise.resolve(null),
    ])
      .then(([labs, listaFazendas, produtor]) => {
        if (!vivo) return
        setLaboratorios(labs.data)
        setPropriedades(listaFazendas.data)
        setErro(null)
        if (!produtor) return
        const dados = produtor.data
        setNome(dados.nome)
        setInscricaoFiscal(dados.inscricaoFiscal)
        setEndereco(dados.endereco)
        setDataInicial(dia(dados.dataInicial))
        setDataFinal(dia(dados.dataFinal))
        setComissaoProducao(String(dados.comissaoProducao))
        setObservacoes((dados.observacoes ?? '').slice(0, LIMITE_OBS))
        setLaboratorio(dados.laboratorio)
        setFazendas(dados.propriedades)
      })
      .catch((falha: unknown) => {
        if (vivo) setErro(mensagemDoErro(falha))
      })
    return () => {
      vivo = false
    }
  }, [id, tentativa])

  async function salvar(evento: FormEvent) {
    evento.preventDefault()
    if (salvando) return
    if (!nome.trim() || !inscricaoFiscal.trim() || !endereco.trim() || !laboratorio) {
      setAviso('Preencha nome, inscrição, endereço e laboratório.')
      return
    }
    if (!dataInicial || !dataFinal || dataFinal < dataInicial) {
      setAviso('Data final não pode ser anterior à data inicial.')
      return
    }
    const comissao = Number(comissaoProducao)
    if (Number.isNaN(comissao) || comissao < 0) {
      setAviso('A comissão não pode ser negativa.')
      return
    }

    const corpo = {
      nome: nome.trim(),
      inscricaoFiscal: inscricaoFiscal.trim(),
      endereco: endereco.trim(),
      dataInicial: paraApi(dataInicial),
      dataFinal: paraApi(dataFinal),
      comissaoProducao: comissao,
      observacoes: observacoes.trim() || null,
      laboratorioId: laboratorio.id,
      propriedadesIds: fazendas.map((item) => item.id),
    }

    setSalvando(true)
    try {
      if (id) await cliente.put(`/api/produtores/${id}`, corpo)
      else await cliente.post('/api/produtores', corpo)
      navegar('/', { state: { sucesso: 'Cadastro salvo com sucesso!' } })
    } catch (falha) {
      setAviso(mensagemDoErro(falha))
      setSalvando(false)
    }
  }

  if (erro) {
    return <Erro mensagem={erro} aoTentar={() => { setErro(null); setLaboratorios(null); setTentativa((valor) => valor + 1) }} />
  }
  if (!laboratorios || !propriedades) return <Carregando />

  return (
    <form id="form-produtor" noValidate onSubmit={(evento) => { void salvar(evento) }}>
      {aviso && <p className="mb-4 text-sm text-red-700">{aviso}</p>}
      <Grid container spacing={3}>
        <Grid size={{ xs: 12, md: 6 }}>
          <TextField
            variant="standard"
            fullWidth
            required
            label="Nome"
            value={nome}
            onChange={(evento) => setNome(evento.target.value)}
          />
        </Grid>
        <Grid size={{ xs: 12, md: 3 }}>
          <CampoData rotulo="Data Inicial" valor={dataInicial} aoMudar={setDataInicial} />
        </Grid>
        <Grid size={{ xs: 12, md: 3 }}>
          <CampoData rotulo="Data Final" valor={dataFinal} aoMudar={setDataFinal} />
        </Grid>
        <Grid size={{ xs: 12, md: 6 }}>
          <Autocomplete
            multiple
            options={propriedades}
            disableCloseOnSelect
            getOptionLabel={(opcao) => opcao.nome}
            isOptionEqualToValue={(a, b) => a.id === b.id}
            value={fazendas}
            onChange={(_, escolhidas) => setFazendas(escolhidas)}
            noOptionsText="Nenhuma propriedade"
            renderValue={(selecionadas, getItemProps) =>
              selecionadas.map((fazenda, indice) => {
                const { key, ...restante } = getItemProps({ index: indice })
                return (
                  <Chip
                    key={key}
                    {...restante}
                    label={fazenda.nome}
                    variant="outlined"
                    size="small"
                    sx={chipFazenda}
                  />
                )
              })
            }
            renderInput={(params) => (
              <TextField
                {...params}
                variant="standard"
                required
                label="Propriedades"
                helperText={fazendas.length > 0 ? `${fazendas.length} selecionada${fazendas.length === 1 ? '' : 's'}` : undefined}
              />
            )}
          />
        </Grid>
        <Grid size={{ xs: 12, md: 6 }}>
          <Autocomplete
            options={laboratorios}
            getOptionLabel={(opcao) => opcao.nome}
            isOptionEqualToValue={(a, b) => a.id === b.id}
            value={laboratorio}
            onChange={(_, escolhido) => setLaboratorio(escolhido)}
            noOptionsText="Nenhum laboratório"
            renderInput={(params) => (
              <TextField {...params} variant="standard" required label="Laboratório" />
            )}
          />
        </Grid>
        <Grid size={{ xs: 12, md: 4 }}>
          <TextField
            variant="standard"
            fullWidth
            required
            label="Inscrição fiscal"
            value={inscricaoFiscal}
            onChange={(evento) => setInscricaoFiscal(evento.target.value)}
          />
        </Grid>
        <Grid size={{ xs: 12, md: 5 }}>
          <TextField
            variant="standard"
            fullWidth
            required
            label="Endereço"
            value={endereco}
            onChange={(evento) => setEndereco(evento.target.value)}
          />
        </Grid>
        <Grid size={{ xs: 12, md: 3 }}>
          <TextField
            variant="standard"
            fullWidth
            required
            label="Comissão"
            type="number"
            value={comissaoProducao}
            onChange={(evento) => setComissaoProducao(evento.target.value)}
            slotProps={{ htmlInput: { min: 0, step: '0.01' } }}
          />
        </Grid>
        <Grid size={12}>
          <TextField
            variant="standard"
            fullWidth
            label="Observações"
            value={observacoes}
            onChange={(evento) => setObservacoes(evento.target.value.slice(0, LIMITE_OBS))}
            helperText={`${observacoes.length}/${LIMITE_OBS}`}
            slotProps={{
              htmlInput: { maxLength: LIMITE_OBS },
              formHelperText: { sx: { textAlign: 'right', mx: 0 } },
            }}
          />
        </Grid>
      </Grid>
    </form>
  )
}
