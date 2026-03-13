import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { login } from '../api/auth'

export default function LoginPage() {
  const [loginId, setLoginId] = useState('')
  const [memberPassword, setMemberPassword] = useState('')
  const [error, setError] = useState('')
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    try {
      const { data } = await login(loginId, memberPassword)
      localStorage.setItem('accessToken', data.accessToken)
      localStorage.setItem('memberId', data.memberId)
      localStorage.setItem('memberName', data.memberName)
      navigate('/posts')
    } catch (err) {
      setError(err.response?.data?.message || 'Login failed')
    }
  }

  return (
    <div className="card" style={{ maxWidth: 400, margin: '60px auto' }}>
      <h2>Login</h2>
      {error && <p className="error">{error}</p>}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Login ID</label>
          <input value={loginId} onChange={(e) => setLoginId(e.target.value)} required />
        </div>
        <div className="form-group">
          <label>Password</label>
          <input type="password" value={memberPassword} onChange={(e) => setMemberPassword(e.target.value)} required />
        </div>
        <button className="btn btn-primary" type="submit">Login</button>
      </form>
      <p style={{ marginTop: 16 }}>
        Don't have an account? <Link to="/signup">Sign Up</Link>
      </p>
    </div>
  )
}
