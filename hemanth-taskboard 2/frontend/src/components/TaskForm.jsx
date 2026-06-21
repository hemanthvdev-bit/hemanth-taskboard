import React, { useState, useEffect } from 'react'

const STATUS_OPTIONS = ['TODO', 'IN_PROGRESS', 'DONE']
const PRIORITY_OPTIONS = ['LOW', 'MEDIUM', 'HIGH']

export default function TaskForm({ task, onSave, onCancel }) {
  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')
  const [status, setStatus] = useState('TODO')
  const [priority, setPriority] = useState('MEDIUM')
  const [dueDate, setDueDate] = useState('')
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    if (task) {
      setTitle(task.title || '')
      setDescription(task.description || '')
      setStatus(task.status || 'TODO')
      setPriority(task.priority || 'MEDIUM')
      setDueDate(task.dueDate || '')
    }
  }, [task])

  const handleSubmit = async (e) => {
    e.preventDefault()
    setSaving(true)
    try {
      await onSave({
        title,
        description,
        status,
        priority,
        dueDate: dueDate || null,
      })
    } finally {
      setSaving(false)
    }
  }

  return (
    <div className="modal-overlay" onClick={onCancel}>
      <form className="modal-content" onSubmit={handleSubmit} onClick={(e) => e.stopPropagation()}>
        <h2>{task ? 'Edit Task' : 'New Task'}</h2>

        <label>
          Title
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
            maxLength={150}
            autoFocus
          />
        </label>

        <label>
          Description
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            maxLength={2000}
            rows={4}
          />
        </label>

        <div className="form-row">
          <label>
            Status
            <select value={status} onChange={(e) => setStatus(e.target.value)}>
              {STATUS_OPTIONS.map((s) => (
                <option key={s} value={s}>{s.replace('_', ' ')}</option>
              ))}
            </select>
          </label>

          <label>
            Priority
            <select value={priority} onChange={(e) => setPriority(e.target.value)}>
              {PRIORITY_OPTIONS.map((p) => (
                <option key={p} value={p}>{p}</option>
              ))}
            </select>
          </label>
        </div>

        <label>
          Due date
          <input
            type="date"
            value={dueDate || ''}
            onChange={(e) => setDueDate(e.target.value)}
          />
        </label>

        <div className="modal-actions">
          <button type="button" onClick={onCancel} className="btn-secondary">
            Cancel
          </button>
          <button type="submit" disabled={saving}>
            {saving ? 'Saving...' : 'Save'}
          </button>
        </div>
      </form>
    </div>
  )
}
