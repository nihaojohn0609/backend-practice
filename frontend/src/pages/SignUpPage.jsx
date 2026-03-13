import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { signup } from '../api/auth'

export default function SignUpPage() {
  const [loginId, setLoginId] = useState('')
  const [memberName, setMemberName] = useState('')
  const [memberPassword, setMemberPassword] = useState('')
  const [error, setError] = useState('')
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    try {
      await signup(loginId, memberName, memberPassword)
      navigate('/login')
    } catch (err) {
      setError(err.response?.data?.message || 'Sign up failed')
    }
  }

  return (
    <div className="card" style={{ maxWidth: 400, margin: '60px auto' }}>
      <h2>Sign Up</h2>
      {error && <p className="error">{error}</p>}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Login ID</label>
          <input value={loginId} onChange={(e) => setLoginId(e.target.value)} required />
        </div>
        <div className="form-group">
          <label>Name</label>
          <input value={memberName} onChange={(e) => setMemberName(e.target.value)} required />
        </div>
        <div className="form-group">
          <label>Password</label>
          <input type="password" value={memberPassword} onChange={(e) => setMemberPassword(e.target.value)} required />
        </div>
        <button className="btn btn-primary" type="submit">Sign Up</button>
      </form>
      <p style={{ marginTop: 16 }}>
        Already have an account? <Link to="/login">Login</Link>
      </p>
    </div>
  )
}
