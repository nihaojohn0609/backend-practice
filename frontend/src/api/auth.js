import api from './axios'

export const signup = (loginId, memberName, memberPassword) =>
  api.post('/auth/signup', { loginId, memberName, memberPassword })

export const login = (loginId, memberPassword) =>
  api.post('/auth/login', { loginId, memberPassword })

export const validateLoginId = (loginId) =>
  api.get('/auth/members/validate', { params: { loginId } })
