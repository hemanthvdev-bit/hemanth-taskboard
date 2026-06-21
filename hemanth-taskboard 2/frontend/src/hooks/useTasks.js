import { useState, useCallback, useEffect } from 'react'
import { taskApi } from '../api/tasks'

export function useTasks(statusFilter) {
  const [tasks, setTasks] = useState([])
  const [stats, setStats] = useState({ todo: 0, inProgress: 0, done: 0 })
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const fetchTasks = useCallback(async () => {
    setLoading(true)
    setError('')
    try {
      const { data } = await taskApi.getTasks(statusFilter)
      setTasks(data.content)
    } catch (err) {
      setError('Failed to load tasks.')
    } finally {
      setLoading(false)
    }
  }, [statusFilter])

  const fetchStats = useCallback(async () => {
    try {
      const { data } = await taskApi.getStats()
      setStats(data)
    } catch (err) {
      // stats are non critical, fail silently
    }
  }, [])

  useEffect(() => {
    fetchTasks()
    fetchStats()
  }, [fetchTasks, fetchStats])

  const createTask = async (task) => {
    await taskApi.createTask(task)
    await fetchTasks()
    await fetchStats()
  }

  const updateTask = async (id, task) => {
    await taskApi.updateTask(id, task)
    await fetchTasks()
    await fetchStats()
  }

  const deleteTask = async (id) => {
    await taskApi.deleteTask(id)
    await fetchTasks()
    await fetchStats()
  }

  return { tasks, stats, loading, error, createTask, updateTask, deleteTask, refetch: fetchTasks }
}
