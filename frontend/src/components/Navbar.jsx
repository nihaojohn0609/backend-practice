import { Link, useNavigate } from 'react-router-dom'

export default function Navbar() {
  const navigate = useNavigate()
  const memberName = localStorage.getItem('memberName')
  const isLoggedIn = !!localStorage.getItem('accessToken')

  const handleLogout = () => {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('memberId')
    localStorage.removeItem('memberName')
    navigate('/login')
  }

  return (
    <nav>
      <div>
        <Link to="/posts">Posts</Link>
        {isLoggedIn && <Link to="/posts/new">New Post</Link>}
        {isLoggedIn && <Link to="/mypage">My Page</Link>}
      </div>
      <div>
        {isLoggedIn ? (
          <>
            <span style={{ color: 'white', marginRight: 12 }}>{memberName}</span>
            <button onClick={handleLogout}>Logout</button>
          </>
        ) : (
          <>
            <Link to="/login">Login</Link>
            <Link to="/signup">Sign Up</Link>
          </>
        )}
      </div>
    </nav>
  )
}
