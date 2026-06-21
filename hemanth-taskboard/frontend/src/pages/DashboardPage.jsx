import React, { useState } from 'react'
import { useTasks } from '../hooks/useTasks'
import TaskCard from '../components/TaskCard'
import TaskForm from '../components/TaskForm'

const FILTERS = [
  { label: 'All', value: '' },
  { label: 'To Do', value: 'TODO' },
  { label: 'In Progress', value: 'IN_PROGRESS' },
  { label: 'Done', value: 'DONE' },
]

export default function DashboardPage() {
  const [statusFilter, setStatusFilter] = useState('')
  const { tasks, stats, loading, error, createTask, updateTask, deleteTask } = useTasks(statusFilter || undefined)
  const [showForm, setShowForm] = useState(false)
  const [editingTask, setEditingTask] = useState(null)

  const openCreateForm = () => {
    setEditingTask(null)
    setShowForm(true)
  }

  const openEditForm = (task) => {
    setEditingTask(task)
    setShowForm(true)
  }

  const closeForm = () => {
    setShowForm(false)
    setEditingTask(null)
  }

  const handleSave = async (taskData) => {
    if (editingTask) {
      await updateTask(editingTask.id, taskData)
    } else {
      await createTask(taskData)
    }
    closeForm()
  }

  const handleDelete = async (id) => {
    if (window.confirm('Delete this task?')) {
      await deleteTask(id)
    }
  }

  const handleStatusChange = async (id, newStatus) => {
    const task = tasks.find((t) => t.id === id)
    if (!task) return
    await updateTask(id, { ...task, status: newStatus })
  }

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <h1>My Tasks</h1>
        <button onClick={openCreateForm}>+ New Task</button>
      </div>

      <div className="stats-bar">
        <div className="stat-item">
          <span className="stat-value">{stats.todo}</span>
          <span className="stat-label">To Do</span>
        </div>
        <div className="stat-item">
          <span className="stat-value">{stats.inProgress}</span>
          <span className="stat-label">In Progress</span>
        </div>
        <div className="stat-item">
          <span className="stat-value">{stats.done}</span>
          <span className="stat-label">Done</span>
        </div>
      </div>

      <div className="filter-bar">
        {FILTERS.map((f) => (
          <button
            key={f.value}
            className={`filter-chip ${statusFilter === f.value ? 'active' : ''}`}
            onClick={() => setStatusFilter(f.value)}
          >
            {f.label}
          </button>
        ))}
      </div>

      {error && <div className="error-banner">{error}</div>}

      {loading ? (
        <p className="loading-text">Loading tasks...</p>
      ) : tasks.length === 0 ? (
        <p className="empty-text">No tasks yet. Create your first one.</p>
      ) : (
        <div className="task-grid">
          {tasks.map((task) => (
            <TaskCard
              key={task.id}
              task={task}
              onEdit={openEditForm}
              onDelete={handleDelete}
              onStatusChange={handleStatusChange}
            />
          ))}
        </div>
      )}

      {showForm && (
        <TaskForm task={editingTask} onSave={handleSave} onCancel={closeForm} />
      )}
    </div>
  )
}
