import { createContext, useContext, useEffect, useMemo, useState } from 'react'
import { api } from '../lib/api'

const AuthContext = createContext(null)
export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)
  useEffect(() => {
    const token = localStorage.getItem('campustrack_token')
    if (!token) { setLoading(false); return }
    api('/auth/me').then(setUser).catch(() => { localStorage.removeItem('campustrack_token'); setUser(null) }).finally(() => setLoading(false))
  }, [])
  const value = useMemo(() => ({ user, loading, async login(credentials) {
    const result = await api('/auth/login', { method: 'POST', body: JSON.stringify(credentials) })
    localStorage.setItem('campustrack_token', result.token)
    const profile = await api('/auth/me')
    setUser(profile)
    return profile
  }, async register(details) {
    const result = await api('/auth/register', { method: 'POST', body: JSON.stringify(details) })
    localStorage.setItem('campustrack_token', result.token)
    const profile = await api('/auth/me')
    setUser(profile)
    return profile
  }, logout() { localStorage.removeItem('campustrack_token'); setUser(null) } }), [user, loading])
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
export const useAuth = () => useContext(AuthContext)
