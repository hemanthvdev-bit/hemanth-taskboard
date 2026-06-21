import apiClient from './client'

export const authApi = {
  login: (username, password) =>
    apiClient.post('/auth/login', { username, password }),

  register: (username, email, password) =>
    apiClient.post('/auth/register', { username, email, password }),
}
