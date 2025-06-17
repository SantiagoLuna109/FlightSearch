import React from 'react'
import '../styles/Pagination.css'

interface Props {
  currentPage: number
  totalPages: number
  onPageChange: (page: number) => void
}

const Pagination: React.FC<Props> = ({ currentPage, totalPages, onPageChange }) => {
  const change = (p: number) => p >= 0 && p < totalPages && onPageChange(p)

  const pages = (): (number | string)[] => {
    if (totalPages <= 5) return Array.from({ length: totalPages }, (_, i) => i + 1)
    const list: (number | string)[] = [1]
    let start = Math.max(2, currentPage + 1 - 1)
    let end = Math.min(totalPages - 1, currentPage + 1 + 1)
    if (currentPage + 1 <= 3) { start = 2; end = 4 }
    if (currentPage + 1 >= totalPages - 2) { start = totalPages - 3; end = totalPages - 1 }
    if (start > 2) list.push('…')
    for (let i = start; i <= end; i++) list.push(i)
    if (end < totalPages - 1) list.push('…')
    list.push(totalPages)
    return list
  }

  return (
    <div className="pagination">
      <button disabled={currentPage === 0} onClick={() => change(0)}>&lt;&lt;</button>
      <button disabled={currentPage === 0} onClick={() => change(currentPage - 1)}>&lt;</button>
      {pages().map((p, i) =>
        typeof p === 'number' ? (
          <button
            key={i}
            onClick={() => change(p - 1)}
            style={{ fontWeight: currentPage === p - 1 ? 'bold' : 'normal' }}
          >
            {p}
          </button>
        ) : (
          <span key={i} className="ellipsis">{p}</span>
        )
      )}
      <button disabled={currentPage === totalPages - 1} onClick={() => change(currentPage + 1)}>&gt;</button>
      <button disabled={currentPage === totalPages - 1} onClick={() => change(totalPages - 1)}>&gt;&gt;</button>
    </div>
  )
}

export default Pagination