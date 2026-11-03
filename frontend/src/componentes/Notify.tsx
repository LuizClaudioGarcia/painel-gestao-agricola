import { useEffect, useState } from 'react'

type Props = {
  mensagem: string | null
  aoFechar: () => void
}

function IconeOk() {
  return (
    <svg width="28" height="28" viewBox="0 0 28 28" aria-hidden="true">
      <circle cx="14" cy="14" r="14" fill="#fff" />
      <path
        d="M8.2 14.4l3.6 3.6 8-8.4"
        fill="none"
        stroke="#4caf50"
        strokeWidth="2.4"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  )
}

function IconeFechar() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" aria-hidden="true">
      <path
        d="M6 6l12 12M18 6L6 18"
        fill="none"
        stroke="currentColor"
        strokeWidth="2"
        strokeLinecap="round"
      />
    </svg>
  )
}

export function Notify({ mensagem, aoFechar }: Props) {
  const [fase, setFase] = useState<'sobe' | 'desce' | null>(null)

  useEffect(() => {
    if (!mensagem) {
      setFase(null)
      return
    }
    setFase('sobe')
    const t = window.setTimeout(() => setFase('desce'), 4000)
    return () => window.clearTimeout(t)
  }, [mensagem])

  if (!mensagem || !fase) return null

  return (
    <div className="pointer-events-none fixed inset-x-0 bottom-6 z-50 flex justify-center px-4">
      <div
        role="status"
        className={`pointer-events-auto flex w-full max-w-3xl items-center gap-4 rounded-lg bg-[#4caf50] px-5 py-3.5 text-[16px] text-white shadow-[0_4px_12px_rgba(0,0,0,0.18)] ${
          fase === 'desce' ? 'notify-desce' : 'notify-sobe'
        }`}
        onAnimationEnd={() => {
          if (fase === 'desce') aoFechar()
        }}
      >
        <span className="shrink-0">
          <IconeOk />
        </span>
        <span className="min-w-0 flex-1">{mensagem}</span>
        <button
          type="button"
          aria-label="Fechar"
          className="shrink-0 rounded p-1 text-white hover:bg-white/15"
          onClick={() => setFase('desce')}
        >
          <IconeFechar />
        </button>
      </div>
    </div>
  )
}
