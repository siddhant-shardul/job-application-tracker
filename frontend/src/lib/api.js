const API_URL = (import.meta.env.VITE_API_URL || 'http://localhost:8080/api').replace(/\/$/, '')

export async function api(path, options = {}) {
  const token = localStorage.getItem('campustrack_token')
  const headers = new Headers(options.headers || {})
  if (token) headers.set('Authorization', `Bearer ${token}`)
  if (options.body && !(options.body instanceof FormData) && !headers.has('Content-Type')) {
    headers.set('Content-Type', 'application/json')
  }
  const response = await fetch(`${API_URL}${path}`, { ...options, headers })
  if (response.status === 204) return null
  const contentType = response.headers.get('content-type') || ''
  const payload = contentType.includes('application/json') ? await response.json() : await response.text()
  if (!response.ok) {
    const error = new Error(payload?.message || payload?.error || payload || `Request failed (${response.status})`)
    error.status = response.status
    throw error
  }
  return payload
}

export const json = (value) => JSON.stringify(value)
export const money = (value) => value == null ? 'Not disclosed' : `₹${Number(value).toLocaleString('en-IN')} LPA`
export const dateLabel = (value) => value ? new Date(`${value}T00:00:00`).toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' }) : 'Not specified'
