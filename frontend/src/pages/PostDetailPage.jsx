import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getPost, deletePost } from '../api/posts'

export default function PostDetailPage() {
  const { postId } = useParams()
  const navigate = useNavigate()
  const [post, setPost] = useState(null)
  const [error, setError] = useState('')
  const memberName = localStorage.getItem('memberName')

  useEffect(() => {
    getPost(postId)
      .then(({ data }) => setPost(data))
      .catch((err) => setError(err.response?.data?.message || 'Failed to load post'))
  }, [postId])

  const handleDelete = async () => {
    if (!window.confirm('Are you sure you want to delete this post?')) return
    try {
      await deletePost(postId)
      navigate('/posts')
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete post')
    }
  }

  if (error) return <p className="error">{error}</p>
  if (!post) return <p>Loading...</p>

  const isOwner = post.writerName === memberName

  return (
    <div className="card">
      <h1>{post.title}</h1>
      <p className="post-meta">by {post.writerName}</p>
      <hr style={{ margin: '16px 0' }} />
      <p style={{ whiteSpace: 'pre-wrap', lineHeight: 1.6 }}>{post.content}</p>
      <div style={{ marginTop: 24 }}>
        <button className="btn btn-secondary" onClick={() => navigate('/posts')}>Back</button>
        {isOwner && (
          <>
            <button className="btn btn-primary" onClick={() => navigate(`/posts/${postId}/edit`)}>Edit</button>
            <button className="btn btn-danger" onClick={handleDelete}>Delete</button>
          </>
        )}
      </div>
    </div>
  )
}
