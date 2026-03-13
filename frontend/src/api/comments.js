import api from './axios'

export const getComments = (postId) =>
  api.get(`/posts/${postId}/comments`)

export const createComment = (postId, content) =>
  api.post(`/posts/${postId}/comments`, { content })

export const updateComment = (postId, commentId, content) =>
  api.put(`/posts/${postId}/comments/${commentId}`, { content })

export const deleteComment = (postId, commentId) =>
  api.delete(`/posts/${postId}/comments/${commentId}`)
