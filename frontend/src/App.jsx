import { Routes, Route, Navigate } from 'react-router-dom'
import Navbar from './components/Navbar'
import LoginPage from './pages/LoginPage'
import SignUpPage from './pages/SignUpPage'
import PostListPage from './pages/PostListPage'
import PostDetailPage from './pages/PostDetailPage'
import PostFormPage from './pages/PostFormPage'
import MyPage from './pages/MyPage'

function PrivateRoute({ children }) {
  const token = localStorage.getItem('accessToken')
  return token ? children : <Navigate to="/login" />
}

export default function App() {
  return (
    <>
      <Navbar />
      <div className="container">
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignUpPage />} />
          <Route path="/posts" element={<PrivateRoute><PostListPage /></PrivateRoute>} />
          <Route path="/posts/new" element={<PrivateRoute><PostFormPage /></PrivateRoute>} />
          <Route path="/posts/:postId" element={<PrivateRoute><PostDetailPage /></PrivateRoute>} />
          <Route path="/posts/:postId/edit" element={<PrivateRoute><PostFormPage /></PrivateRoute>} />
          <Route path="/mypage" element={<PrivateRoute><MyPage /></PrivateRoute>} />
          <Route path="*" element={<Navigate to="/posts" />} />
        </Routes>
      </div>
    </>
  )
}
