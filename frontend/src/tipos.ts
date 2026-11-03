export type Cadastro = {
  id: string
  codigo: number
  nome: string
  inscricaoFiscal: string
  endereco: string
}

export type Produtor = {
  id: string
  codigo: number
  nome: string
  inscricaoFiscal: string
  endereco: string
  dataInicial: string
  dataFinal: string
  observacoes: string | null
  comissaoProducao: number
  laboratorio: Cadastro
  propriedades: Cadastro[]
}

export type LinhaPainel = {
  codigo: number
  nome: string
  quantidadeProdutores: number
  valor: number
}
