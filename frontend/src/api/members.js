import api from './axios'

export const getAllMembers = () => api.get('/members')

export const getMember = (memberId) => api.get(`/members/${memberId}`)

export const updateMember = (memberId, memberName, password) =>
  api.put(`/members/${memberId}`, { memberName, password })

export const deleteMember = (memberId) => api.delete(`/members/${memberId}`)
