import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { getAllPosts } from '../api/posts'

export default function PostListPage() {
  const [posts, setPosts] = useState([])
  const [error, setError] = useState('')

  useEffect(() => {
    getAllPosts()
      .then(({ data }) => setPosts(data))
      .catch((err) => setError(err.response?.data?.message || 'Failed to load posts'))
  }, [])

  return (
    <>
      <h1>Posts</h1>
      {error && <p className="error">{error}</p>}
      {posts.length === 0 && !error && <p>No posts yet.</p>}
      {posts.map((post) => (
        <Link to={`/posts/${post.postId}`} key={post.postId} style={{ textDecoration: 'none', color: 'inherit' }}>
          <div className="card" style={{ cursor: 'pointer' }}>
            <h3>{post.title}</h3>
            <p className="post-meta">by {post.writerName}</p>
          </div>
        </Link>
      ))}
    </>
  )
}
