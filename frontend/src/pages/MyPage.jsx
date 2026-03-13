import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { getMember, updateMember, deleteMember } from '../api/members'

export default function MyPage() {
  const navigate = useNavigate()
  const memberId = localStorage.getItem('memberId')
  const [memberName, setMemberName] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  useEffect(() => {
    getMember(memberId)
      .then(({ data }) => setMemberName(data.memberName))
      .catch((err) => setError(err.response?.data?.message || 'Failed to load profile'))
  }, [memberId])

  const handleUpdate = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')
    try {
      const { data } = await updateMember(memberId, memberName, password || null)
      localStorage.setItem('memberName', data.memberName)
      setPassword('')
      setSuccess('Profile updated successfully')
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update profile')
    }
  }

  const handleDelete = async () => {
    if (!window.confirm('Are you sure you want to delete your account? This cannot be undone.')) return
    try {
      await deleteMember(memberId)
      localStorage.removeItem('accessToken')
      localStorage.removeItem('memberId')
      localStorage.removeItem('memberName')
      navigate('/login')
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete account')
    }
  }

  return (
    <div className="card" style={{ maxWidth: 400, margin: '40px auto' }}>
      <h2>My Page</h2>
      {error && <p className="error">{error}</p>}
      {success && <p style={{ color: 'green', marginBottom: 12 }}>{success}</p>}
      <form onSubmit={handleUpdate}>
        <div className="form-group">
          <label>Name</label>
          <input value={memberName} onChange={(e) => setMemberName(e.target.value)} />
        </div>
        <div className="form-group">
          <label>New Password (leave blank to keep current)</label>
          <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} />
        </div>
        <button className="btn btn-primary" type="submit">Update</button>
      </form>
      <hr style={{ margin: '24px 0' }} />
      <button className="btn btn-danger" onClick={handleDelete}>Delete Account</button>
    </div>
  )
}
