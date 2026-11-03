import { useEffect, useState } from 'react'
import { createPortal } from 'react-dom'

export type TipoNotify = 'sucesso' | 'erro'

type Props = {
  mensagem: string | null
  tipo?: TipoNotify
  aoFechar: () => void
}

const COR = {
  sucesso: { barra: 'bg-[#4caf50]', icone: '#4caf50' },
  erro: { barra: 'bg-[#e53935]', icone: '#e53935' },
}

function IconeStatus({ tipo, cor }: { tipo: TipoNotify; cor: string }) {
  return (
    <svg width="22" height="22" viewBox="0 0 22 22" aria-hidden="true">
      <circle cx="11" cy="11" r="11" fill="#fff" />
      {tipo === 'sucesso' ? (
        <path
          d="M6.2 11.4l3 3 6.6-7"
          fill="none"
          stroke={cor}
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
      ) : (
        <>
          <path d="M11 6.2v6.2" fill="none" stroke={cor} strokeWidth="2" strokeLinecap="round" />
          <circle cx="11" cy="15.6" r="1.05" fill={cor} />
        </>
      )}
    </svg>
  )
}

function IconeFechar() {
  return (
    <svg width="14" height="14" viewBox="0 0 24 24" aria-hidden="true">
      <path
        d="M6 6l12 12M18 6L6 18"
        fill="none"
        stroke="currentColor"
        strokeWidth="2.2"
        strokeLinecap="round"
      />
    </svg>
  )
}

export function Notify({ mensagem, tipo = 'sucesso', aoFechar }: Props) {
  const [fase, setFase] = useState<'sobe' | 'desce' | null>(null)
  const visual = COR[tipo]

  useEffect(() => {
    if (!mensagem) {
      setFase(null)
      return
    }
    setFase('sobe')
    const t = window.setTimeout(() => setFase('desce'), tipo === 'erro' ? 5000 : 4000)
    return () => window.clearTimeout(t)
  }, [mensagem, tipo])

  if (!mensagem || !fase) return null

  return createPortal(
    <div className="pointer-events-none fixed inset-x-0 bottom-5 z-50 flex justify-center px-3">
      <div
        role="status"
        className={`pointer-events-auto inline-flex max-w-[calc(100%-1.5rem)] items-center rounded-md px-3.5 py-2 text-[15px] leading-none text-white shadow-[0_2px_8px_rgba(0,0,0,0.16)] ${visual.barra} ${
          fase === 'desce' ? 'notify-desce' : 'notify-sobe'
        }`}
        onAnimationEnd={() => {
          if (fase === 'desce') aoFechar()
        }}
      >
        <span className="shrink-0">
          <IconeStatus tipo={tipo} cor={visual.icone} />
        </span>
        <span className="ml-2.5 min-w-0">{mensagem}</span>
        <button
          type="button"
          aria-label="Fechar"
          className="ml-8 shrink-0 rounded p-0.5 text-white hover:bg-white/15"
          onClick={() => setFase('desce')}
        >
          <IconeFechar />
        </button>
      </div>
    </div>,
    document.body,
  )
}
