import api from './axios'

export const getAllPosts = () => api.get('/posts')

export const getPost = (postId) => api.get(`/posts/${postId}`)

export const createPost = (title, content) =>
  api.post('/posts', { title, content })

export const updatePost = (postId, title, content) =>
  api.put(`/posts/${postId}`, { title, content })

export const deletePost = (postId) => api.delete(`/posts/${postId}`)
