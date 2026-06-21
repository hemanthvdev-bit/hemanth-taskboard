import React, { createContext, useContext, useState, useCallback } from 'react'
import { authApi } from '../api/auth'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const username = localStorage.getItem('username')
    const role = localStorage.getItem('role')
    return username ? { username, role } : null
  })

  const login = useCallback(async (username, password) => {
    const { data } = await authApi.login(username, password)
    localStorage.setItem('token', data.token)
    localStorage.setItem('username', data.username)
    localStorage.setItem('role', data.role)
    setUser({ username: data.username, role: data.role })
    return data
  }, [])

  const register = useCallback(async (username, email, password) => {
    const { data } = await authApi.register(username, email, password)
    localStorage.setItem('token', data.token)
    localStorage.setItem('username', data.username)
    localStorage.setItem('role', data.role)
    setUser({ username: data.username, role: data.role })
    return data
  }, [])

  const logout = useCallback(() => {
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    localStorage.removeItem('role')
    setUser(null)
  }, [])

  return (
    <AuthContext.Provider value={{ user, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}
