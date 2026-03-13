import { useEffect, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { getPost, deletePost } from '../api/posts'
import { getComments, createComment, updateComment, deleteComment } from '../api/comments'

export default function PostDetailPage() {
  const { postId } = useParams()
  const navigate = useNavigate()
  const [post, setPost] = useState(null)
  const [comments, setComments] = useState([])
  const [newComment, setNewComment] = useState('')
  const [editingId, setEditingId] = useState(null)
  const [editContent, setEditContent] = useState('')
  const [error, setError] = useState('')
  const memberName = localStorage.getItem('memberName')

  useEffect(() => {
    getPost(postId)
      .then(({ data }) => setPost(data))
      .catch((err) => setError(err.response?.data?.message || 'Failed to load post'))
    loadComments()
  }, [postId])

  const loadComments = () => {
    getComments(postId)
      .then(({ data }) => setComments(data))
      .catch(() => {})
  }

  const handleAddComment = async (e) => {
    e.preventDefault()
    if (!newComment.trim()) return
    try {
      await createComment(postId, newComment)
      setNewComment('')
      loadComments()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to add comment')
    }
  }

  const handleUpdateComment = async (commentId) => {
    if (!editContent.trim()) return
    try {
      await updateComment(postId, commentId, editContent)
      setEditingId(null)
      setEditContent('')
      loadComments()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update comment')
    }
  }

  const handleDeleteComment = async (commentId) => {
    if (!window.confirm('Delete this comment?')) return
    try {
      await deleteComment(postId, commentId)
      loadComments()
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to delete comment')
    }
  }

  const handleDeletePost = async () => {
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
    <>
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
              <button className="btn btn-danger" onClick={handleDeletePost}>Delete</button>
            </>
          )}
        </div>
      </div>

      <div style={{ marginTop: 24 }}>
        <h2>Comments ({comments.length})</h2>

        <form onSubmit={handleAddComment} style={{ display: 'flex', gap: 8, marginBottom: 16 }}>
          <input
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
            placeholder="Write a comment..."
            style={{ flex: 1, padding: 10, border: '1px solid #ddd', borderRadius: 4 }}
          />
          <button className="btn btn-primary" type="submit">Post</button>
        </form>

        {comments.map((c) => (
          <div className="card" key={c.commentId} style={{ padding: 14 }}>
            {editingId === c.commentId ? (
              <div style={{ display: 'flex', gap: 8 }}>
                <input
                  value={editContent}
                  onChange={(e) => setEditContent(e.target.value)}
                  style={{ flex: 1, padding: 8, border: '1px solid #ddd', borderRadius: 4 }}
                />
                <button className="btn btn-primary" onClick={() => handleUpdateComment(c.commentId)}>Save</button>
                <button className="btn btn-secondary" onClick={() => setEditingId(null)}>Cancel</button>
              </div>
            ) : (
              <>
                <p style={{ marginBottom: 6 }}>{c.content}</p>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <span className="post-meta">by {c.author}</span>
                  {c.author === memberName && (
                    <div>
                      <button
                        className="btn btn-secondary"
                        style={{ padding: '4px 10px', fontSize: 12 }}
                        onClick={() => { setEditingId(c.commentId); setEditContent(c.content) }}
                      >Edit</button>
                      <button
                        className="btn btn-danger"
                        style={{ padding: '4px 10px', fontSize: 12 }}
                        onClick={() => handleDeleteComment(c.commentId)}
                      >Delete</button>
                    </div>
                  )}
                </div>
              </>
            )}
          </div>
        ))}
      </div>
    </>
  )
}
