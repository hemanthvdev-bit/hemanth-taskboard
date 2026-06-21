import apiClient from './client'

export const taskApi = {
  getTasks: (status, page = 0, size = 20) => {
    const params = { page, size }
    if (status) params.status = status
    return apiClient.get('/tasks', { params })
  },

  getTask: (id) => apiClient.get(`/tasks/${id}`),

  createTask: (task) => apiClient.post('/tasks', task),

  updateTask: (id, task) => apiClient.put(`/tasks/${id}`, task),

  deleteTask: (id) => apiClient.delete(`/tasks/${id}`),

  getStats: () => apiClient.get('/tasks/stats'),
}
