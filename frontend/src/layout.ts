export const LINHAS_VISIVEIS = 10
export const ALTURA_LINHA = 52
export const ALTURA_CABECALHO = 44
export const ALTURA_TABELA = ALTURA_CABECALHO + LINHAS_VISIVEIS * ALTURA_LINHA
export const ALTURA_TOOLBAR = 44
export const ALTURA_PAGINACAO = 53
export const ALTURA_BARRA = 46
export const ALTURA_MAIN_PAD = 40
export const ALTURA_QUADRO =
  ALTURA_BARRA + ALTURA_MAIN_PAD + ALTURA_TOOLBAR + ALTURA_TABELA + ALTURA_PAGINACAO

export const linhaFixa = {
  height: ALTURA_LINHA,
  '& .MuiTableCell-root': { height: ALTURA_LINHA, py: 0, boxSizing: 'border-box' },
}
