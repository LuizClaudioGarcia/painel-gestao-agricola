import axios from 'axios'

type ErroCampo = {
  campo?: string
  mensagem?: string
}

type Corpo = {
  mensagem?: string
  erros?: ErroCampo[]
}

const ROTULOS: Record<string, string> = {
  nome: 'Nome',
  inscricaoFiscal: 'Inscrição fiscal',
  endereco: 'Endereço',
  dataInicial: 'Data inicial',
  dataFinal: 'Data final',
  comissaoProducao: 'Comissão',
  observacoes: 'Observações',
  laboratorioId: 'Laboratório',
  propriedadesIds: 'Propriedades',
  quantidadeMinima: 'Quantidade mínima',
  dataInicialDe: 'Data inicial de',
  dataInicialAte: 'Data inicial até',
  dataFinalDe: 'Data final de',
  dataFinalAte: 'Data final até',
  id: 'Identificador',
}

function rotulo(campo: string): string {
  const raiz = campo.replace(/\[\d+]/g, '').split('.')[0]
  return ROTULOS[raiz] ?? campo
}

export function mensagemDoErro(erro: unknown): string {
  if (!axios.isAxiosError(erro) || erro.response == null) {
    return 'API indisponível'
  }
  const corpo = erro.response.data as Corpo | undefined
  if (!corpo?.mensagem) {
    return 'API indisponível'
  }
  const linhas = (corpo.erros ?? []).flatMap((item) => {
    if (!item.campo || !item.mensagem) {
      return []
    }
    return [`${rotulo(item.campo)}: ${item.mensagem}`]
  })
  if (linhas.length > 0) {
    return linhas.join('. ')
  }
  return corpo.mensagem
}
