import { useState } from 'react'
import { Link, useLocation, useNavigate } from 'react-router-dom'
import { ArrowLeft, ArrowRight, GraduationCap, LockKeyhole, Mail, UserRound } from 'lucide-react'
import { useAuth } from '../context/AuthContext'
import { Notice } from '../components/Shared'

function AuthShell({ children, heading, caption }) { return <div className="auth-page"><div className="auth-aside"><Link to="/" className="brand"><div className="brand-mark"><GraduationCap size={22} /></div><div><strong>CampusTrack</strong><small>PLACEMENT PORTAL</small></div></Link><div className="auth-aside-copy"><div className="eyebrow light">YOUR NEXT CHAPTER</div><h1>Make your<br />next move<br /><span>with clarity.</span></h1><p>One place for your campus opportunities, application updates and career details.</p></div><div className="auth-aside-foot">Student Placement Management · Built for campus life</div></div><div className="auth-main"><Link to="/" className="back-home"><ArrowLeft size={16} /> Back to home</Link><div className="auth-card"><div className="eyebrow">WELCOME TO CAMPUS TRACK</div><h2>{heading}</h2><p className="auth-caption">{caption}</p>{children}</div><div className="auth-bottom">Need help? Contact your college placement coordinator.</div></div></div> }

export function Login() {
  const [form, setForm] = useState({ email: '', password: '' }); const [error, setError] = useState(''); const [busy, setBusy] = useState(false)
  const { login } = useAuth(); const navigate = useNavigate(); const location = useLocation()
  const change = e => setForm({ ...form, [e.target.name]: e.target.value })
  async function submit(e) { e.preventDefault(); setError(''); setBusy(true); try { await login(form); navigate(location.state?.from?.pathname || '/dashboard', { replace: true }) } catch (err) { setError(err.message) } finally { setBusy(false) } }
  return <AuthShell heading="Welcome back" caption="Sign in to continue to your placement workspace."><Notice>{error}</Notice><form className="form-stack" onSubmit={submit}><label>Email address<div className="input-wrap"><Mail size={17} /><input name="email" type="email" placeholder="you@college.edu" value={form.email} onChange={change} autoComplete="email" required /></div></label><label>Password<div className="input-wrap"><LockKeyhole size={17} /><input name="password" type="password" placeholder="Enter your password" value={form.password} onChange={change} autoComplete="current-password" required /></div></label><button className="btn btn-primary btn-full" disabled={busy}>{busy ? 'Signing in...' : 'Sign in'} <ArrowRight size={17} /></button></form><div className="auth-switch">New to CampusTrack? <Link to="/register">Create an account</Link></div></AuthShell>
}

export function Register() {
  const [form, setForm] = useState({ fullName: '', email: '', password: '' }); const [error, setError] = useState(''); const [busy, setBusy] = useState(false)
  const { register } = useAuth(); const navigate = useNavigate()
  const change = e => setForm({ ...form, [e.target.name]: e.target.value })
  async function submit(e) { e.preventDefault(); setError(''); setBusy(true); try { await register(form); navigate('/profile', { replace: true }) } catch (err) { setError(err.message) } finally { setBusy(false) } }
  return <AuthShell heading="Create your account" caption="A few details to get your placement journey started."><Notice>{error}</Notice><form className="form-stack" onSubmit={submit}><label>Full name<div className="input-wrap"><UserRound size={17} /><input name="fullName" placeholder="Your full name" value={form.fullName} onChange={change} maxLength="100" required /></div></label><label>Email address<div className="input-wrap"><Mail size={17} /><input name="email" type="email" placeholder="you@college.edu" value={form.email} onChange={change} required /></div></label><label>Password<div className="input-wrap"><LockKeyhole size={17} /><input name="password" type="password" placeholder="At least 8 characters" minLength="8" maxLength="72" value={form.password} onChange={change} required /></div></label><p className="form-hint">Your account will be created with student access. Administrator access is assigned separately by the placement team.</p><button className="btn btn-primary btn-full" disabled={busy}>{busy ? 'Creating account...' : 'Create account'} <ArrowRight size={17} /></button></form><div className="auth-switch">Already have an account? <Link to="/login">Sign in</Link></div></AuthShell>
}
