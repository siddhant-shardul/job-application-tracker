import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { useState } from 'react'
import { BriefcaseBusiness, Building2, ChevronRight, ClipboardList, GraduationCap, LayoutDashboard, LogOut, Menu, Settings2, Users, X } from 'lucide-react'
import { useAuth } from '../context/AuthContext'

export default function AppLayout() {
  const { user, logout } = useAuth(); const navigate = useNavigate(); const [open, setOpen] = useState(false)
  const admin = user?.role === 'ADMIN'
  const links = admin ? [
    { to: '/dashboard', label: 'Overview', icon: LayoutDashboard },
    { to: '/admin/jobs', label: 'Job postings', icon: BriefcaseBusiness },
    { to: '/admin/companies', label: 'Companies', icon: Building2 },
    { to: '/admin/applications', label: 'Applications', icon: ClipboardList },
    { to: '/admin/students', label: 'Students', icon: Users },
  ] : [
    { to: '/dashboard', label: 'Overview', icon: LayoutDashboard },
    { to: '/jobs', label: 'Find opportunities', icon: BriefcaseBusiness },
    { to: '/applications', label: 'My applications', icon: ClipboardList },
    { to: '/profile', label: 'My profile', icon: Settings2 },
  ]
  const signOut = () => { logout(); navigate('/') }
  return <div className="app-shell">
    <aside className={`sidebar ${open ? 'sidebar-open' : ''}`}>
      <div className="brand"><div className="brand-mark"><GraduationCap size={22} /></div><div><strong>CampusTrack</strong><small>PLACEMENT PORTAL</small></div><button className="mobile-close" onClick={() => setOpen(false)}><X /></button></div>
      <div className="side-label">WORKSPACE</div>
      <nav className="side-nav">{links.map(({ to, label, icon: Icon }) => <NavLink key={to} to={to} onClick={() => setOpen(false)} className={({ isActive }) => `side-link ${isActive ? 'active' : ''}`}><Icon size={18} /><span>{label}</span>{label === 'Find opportunities' && <ChevronRight className="link-arrow" size={15} />}</NavLink>)}</nav>
      <div className="sidebar-bottom"><div className="help-card"><div className="help-dot" /><strong>{admin ? 'Placement team' : 'Your next step'}</strong><p>{admin ? 'Keep opportunities and student applications up to date.' : 'A complete profile helps recruiters understand your skills.'}</p>{!admin && <NavLink to="/profile">Complete profile <ChevronRight size={14} /></NavLink>}</div><button className="side-link signout" onClick={signOut}><LogOut size={18} /><span>Sign out</span></button></div>
    </aside>
    {open && <div className="sidebar-scrim" onClick={() => setOpen(false)} />}
    <main className="main-area">
      <header className="topbar"><button className="menu-btn" onClick={() => setOpen(true)}><Menu /></button><div className="breadcrumb"><span>Workspace</span><ChevronRight size={14} /><strong>{links.find(l => location.pathname === l.to || location.pathname.startsWith(l.to + '/'))?.label || 'Overview'}</strong></div><div className="top-user"><div className="avatar">{user?.fullName?.[0]?.toUpperCase() || user?.email?.[0]?.toUpperCase()}</div><div><strong>{user?.fullName || 'User'}</strong><small>{admin ? 'Administrator' : 'Student account'}</small></div></div></header>
      <div className="page-content"><Outlet /></div>
      <footer className="app-footer"><span>CampusTrack · Student Placement Management</span><span>Built for campus placements</span></footer>
    </main>
  </div>
}
