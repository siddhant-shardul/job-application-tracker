import { useState } from 'react'
import { AlertCircle, CheckCircle2, LoaderCircle, Search, X } from 'lucide-react'

export function Spinner({ label = 'Loading...' }) { return <div className="spinner-wrap"><LoaderCircle className="spin" size={22} /><span>{label}</span></div> }
export function Notice({ type = 'error', children, onClose }) {
  if (!children) return null
  const Icon = type === 'success' ? CheckCircle2 : AlertCircle
  return <div className={`notice ${type}`}><Icon size={18} /><span>{children}</span>{onClose && <button className="icon-btn" onClick={onClose} aria-label="Dismiss"><X size={16} /></button>}</div>
}
export function EmptyState({ title = 'Nothing here yet', text = 'When there is something to show, it will appear here.' }) { return <div className="empty-state"><div className="empty-icon">⌁</div><h3>{title}</h3><p>{text}</p></div> }
export function PageTitle({ eyebrow, title, description, action }) { return <div className="page-heading"><div><div className="eyebrow">{eyebrow}</div><h1>{title}</h1>{description && <p>{description}</p>}</div>{action && <div className="heading-action">{action}</div>}</div> }
export function SearchBox({ value, onChange, placeholder = 'Search...' }) { return <label className="search-box"><Search size={17} /><input value={value} onChange={e => onChange(e.target.value)} placeholder={placeholder} /></label> }
export function StatusBadge({ status }) { const key = (status || 'APPLIED').toLowerCase().replace('_', '-'); return <span className={`status-badge ${key}`}>{(status || 'Applied').replaceAll('_', ' ')}</span> }
export function Modal({ title, onClose, children, wide = false }) { return <div className="modal-backdrop" onMouseDown={e => e.target === e.currentTarget && onClose()}><div className={`modal ${wide ? 'wide' : ''}`}><div className="modal-head"><h2>{title}</h2><button className="icon-btn" onClick={onClose}><X size={18} /></button></div>{children}</div></div> }
export function useFlash() {
  const [flash, setFlash] = useState(null)
  const show = (type, message) => { setFlash({ type, message }); window.setTimeout(() => setFlash(null), 4200) }
  return { flash, show, clear: () => setFlash(null) }
}
