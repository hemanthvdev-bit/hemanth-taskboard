import React from 'react'
import { useAuth } from '../context/AuthContext'

export default function Navbar() {
  const { user, logout } = useAuth()

  return (
    <nav className="navbar">
      <div className="navbar-brand">Taskboard</div>
      <div className="navbar-user">
        <span>{user?.username}</span>
        <button onClick={logout} className="btn-link">Logout</button>
      </div>
    </nav>
  )
}
