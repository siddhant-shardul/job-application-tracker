import React from 'react'
import ReactDOM from 'react-dom/client'
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import ProtectedRoute from './components/ProtectedRoute'
import AppLayout from './components/AppLayout'
import Landing from './pages/Landing'
import { Login, Register } from './pages/Auth'
import { AdminCompanies, AdminJobs, Applications, Dashboard, JobDetail, Jobs, Profile, Students } from './pages/Workspace'
import './styles.css'

function App() {
  return <AuthProvider><BrowserRouter><Routes>
    <Route path="/" element={<Landing />} />
    <Route path="/login" element={<Login />} />
    <Route path="/register" element={<Register />} />
    <Route element={<ProtectedRoute />}><Route element={<AppLayout />}>
      <Route path="/dashboard" element={<Dashboard />} />
      <Route path="/applications" element={<Applications />} />
      <Route element={<ProtectedRoute roles={['STUDENT']} />}>
        <Route path="/jobs" element={<Jobs />} />
        <Route path="/jobs/:id" element={<JobDetail />} />
        <Route path="/profile" element={<Profile />} />
      </Route>
      <Route element={<ProtectedRoute roles={['ADMIN']} />}>
        <Route path="/admin/jobs" element={<AdminJobs />} />
        <Route path="/admin/companies" element={<AdminCompanies />} />
        <Route path="/admin/applications" element={<Applications />} />
        <Route path="/admin/students" element={<Students />} />
      </Route>
    </Route></Route>
    <Route path="*" element={<Navigate to="/" replace />} />
  </Routes></BrowserRouter></AuthProvider>
}

ReactDOM.createRoot(document.getElementById('root')).render(<React.StrictMode><App /></React.StrictMode>)
