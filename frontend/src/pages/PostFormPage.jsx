import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { createPost, getPost, updatePost } from '../api/posts'

export default function PostFormPage() {
  const { postId } = useParams()
  const isEdit = !!postId
  const navigate = useNavigate()
  const [title, setTitle] = useState('')
  const [content, setContent] = useState('')
  const [error, setError] = useState('')

  useEffect(() => {
    if (isEdit) {
      getPost(postId)
        .then(({ data }) => {
          setTitle(data.title)
          setContent(data.content)
        })
        .catch((err) => setError(err.response?.data?.message || 'Failed to load post'))
    }
  }, [postId, isEdit])

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    try {
      if (isEdit) {
        await updatePost(postId, title, content)
        navigate(`/posts/${postId}`)
      } else {
        const { data } = await createPost(title, content)
        navigate(`/posts/${data.postId}`)
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save post')
    }
  }

  return (
    <div className="card" style={{ maxWidth: 600, margin: '40px auto' }}>
      <h2>{isEdit ? 'Edit Post' : 'New Post'}</h2>
      {error && <p className="error">{error}</p>}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Title</label>
          <input value={title} onChange={(e) => setTitle(e.target.value)} required />
        </div>
        <div className="form-group">
          <label>Content</label>
          <textarea value={content} onChange={(e) => setContent(e.target.value)} required />
        </div>
        <button className="btn btn-primary" type="submit">{isEdit ? 'Update' : 'Create'}</button>
        <button className="btn btn-secondary" type="button" onClick={() => navigate(-1)}>Cancel</button>
      </form>
    </div>
  )
}
