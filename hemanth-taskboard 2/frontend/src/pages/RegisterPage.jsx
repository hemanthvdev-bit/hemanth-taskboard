import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function RegisterPage() {
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { register } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      await register(username, email, password)
      navigate('/')
    } catch (err) {
      setError(err.response?.data?.error || 'Registration failed.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-page">
      <form className="auth-form" onSubmit={handleSubmit}>
        <h1>Create account</h1>
        {error && <div className="error-banner">{error}</div>}
        <label>
          Username
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
            minLength={3}
            maxLength={30}
            autoFocus
          />
        </label>
        <label>
          Email
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </label>
        <label>
          Password
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            minLength={8}
          />
        </label>
        <button type="submit" disabled={loading}>
          {loading ? 'Creating account...' : 'Create account'}
        </button>
        <p className="auth-switch">
          Already have an account? <Link to="/login">Sign in</Link>
        </p>
      </form>
    </div>
  )
}
