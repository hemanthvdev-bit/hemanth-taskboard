import React from 'react'

const PRIORITY_CLASS = {
  LOW: 'priority-low',
  MEDIUM: 'priority-medium',
  HIGH: 'priority-high',
}

export default function TaskCard({ task, onEdit, onDelete, onStatusChange }) {
  return (
    <div className="task-card">
      <div className="task-card-header">
        <h3>{task.title}</h3>
        <span className={`priority-badge ${PRIORITY_CLASS[task.priority]}`}>
          {task.priority}
        </span>
      </div>

      {task.description && <p className="task-description">{task.description}</p>}

      {task.dueDate && (
        <p className="task-due-date">Due {new Date(task.dueDate).toLocaleDateString()}</p>
      )}

      <div className="task-card-footer">
        <select
          value={task.status}
          onChange={(e) => onStatusChange(task.id, e.target.value)}
          className="status-select"
        >
          <option value="TODO">To Do</option>
          <option value="IN_PROGRESS">In Progress</option>
          <option value="DONE">Done</option>
        </select>

        <div className="task-actions">
          <button onClick={() => onEdit(task)} className="btn-link">Edit</button>
          <button onClick={() => onDelete(task.id)} className="btn-link btn-danger">Delete</button>
        </div>
      </div>
    </div>
  )
}
