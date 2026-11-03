import type { ReactNode } from 'react'

type QuadroProps = {
  titulo: string
  children: ReactNode
  aoFechar: () => void
}

export function ModalQuadro({ titulo, children, aoFechar }: QuadroProps) {
  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black/40 p-4">
      <div className="flex w-full max-w-lg flex-col overflow-hidden rounded bg-white shadow">
        <h2 className="bg-[#00735D] px-4 py-3 text-lg font-semibold text-white">{titulo}</h2>
        <div className="min-h-[160px] px-4 py-4 text-[#323E48]">{children}</div>
        <div className="flex justify-end bg-slate-100 px-4 py-3">
          <button type="button" onClick={aoFechar} className="rounded bg-[#00735D] px-4 py-2 text-sm font-medium tracking-wide text-white">
            FECHAR
          </button>
        </div>
      </div>
    </div>
  )
}

type ListaProps = {
  titulo: string
  itens: string[]
  aoFechar: () => void
}

export function ModalLista({ titulo, itens, aoFechar }: ListaProps) {
  return (
    <ModalQuadro titulo={titulo} aoFechar={aoFechar}>
      <ul className="-mx-4 -my-4">
        {itens.map((item, indice) => (
          <li key={`${indice}-${item}`} className={indice % 2 === 0 ? 'bg-white px-4 py-3' : 'bg-slate-100 px-4 py-3'}>
            {item}
          </li>
        ))}
      </ul>
    </ModalQuadro>
  )
}

type ModalProps = {
  titulo: string
  texto: string
  confirmar: string
  aoCancelar: () => void
  aoConfirmar: () => void
}

export function Modal({ titulo, texto, confirmar, aoCancelar, aoConfirmar }: ModalProps) {
  return (
    <div className="fixed inset-0 flex items-center justify-center bg-black/40 p-4">
      <div className="w-full max-w-md rounded bg-white p-4 shadow">
        <h2 className="text-lg font-semibold text-slate-900">{titulo}</h2>
        <p className="mt-2 text-slate-700">{texto}</p>
        <div className="mt-4 flex justify-end gap-2">
          <button type="button" onClick={aoCancelar} className="rounded border px-3 py-2 text-sm">
            Cancelar
          </button>
          <button type="button" onClick={aoConfirmar} className="rounded bg-red-700 px-3 py-2 text-sm text-white">
            {confirmar}
          </button>
        </div>
      </div>
    </div>
  )
}
