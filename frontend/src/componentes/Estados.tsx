type ErroProps = {
  mensagem: string
  aoTentar: () => void
}

export function Carregando() {
  return <p className="py-8 text-slate-600">Carregando...</p>
}

export function Erro({ mensagem, aoTentar }: ErroProps) {
  return (
    <div className="py-8">
      <p className="text-red-700">{mensagem}</p>
      <button
        type="button"
        onClick={aoTentar}
        className="mt-3 rounded bg-[#00735D] px-3 py-2 text-sm text-white"
      >
        Tentar de novo
      </button>
    </div>
  )
}

export function Vazio({ texto }: { texto: string }) {
  return <p className="py-8 text-slate-600">{texto}</p>
}
