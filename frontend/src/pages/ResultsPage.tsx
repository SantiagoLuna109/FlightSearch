import { Link, useLocation } from 'react-router-dom'
import { useState, useMemo } from 'react'
import FlightCard from '../components/FlightCard'
import Pagination from '../components/Pagination'

const PAGE_SIZE = 10

export default function ResultsPage() {
  const { state } = useLocation() as { state: { offers: any[]; search: Record<string, any> } }
  const offers = state?.offers ?? []
  const search = state?.search ?? {}

  const durMinutes = (isoDur: string) => {
    const [, h = '0', m = '0'] = isoDur.match(/PT(?:(\d+)H)?(?:(\d+)M)?/) || []
    return parseInt(h, 10) * 60 + parseInt(m, 10)
  }

  const [priceAsc, setPriceAsc] = useState(true)
  const [durAsc, setDurAsc] = useState(true)
  const [page, setPage] = useState(0)

  const sorted = useMemo(() => {
    const priceVal = (o: any) => Number(o.price.grandTotal)
    const durVal = (o: any) => o.itineraries.reduce((sum: number, it: any) => sum + durMinutes(it.duration), 0)

    return [...offers].sort((a, b) => {
      const p = priceAsc ? priceVal(a) - priceVal(b) : priceVal(b) - priceVal(a)
      if (p !== 0) return p
      return durAsc ? durVal(a) - durVal(b) : durVal(b) - durVal(a)
    })
  }, [offers, priceAsc, durAsc])

  const totalPages = Math.ceil(sorted.length / PAGE_SIZE)
  const slice = sorted.slice(page * PAGE_SIZE, page * PAGE_SIZE + PAGE_SIZE)

  if (!sorted.length)
    return (
      <div className="p-6 text-center">
        <p className="mb-4">No offers returned. Try another search.</p>
        <Link to="/" className="text-blue-600 underline">Return to search</Link>
      </div>
    )

  return (
    <div className="min-h-screen bg-slate-50 p-6 space-y-6 max-w-3xl mx-auto">
      <button onClick={() => window.history.back()} className="mb-4 px-3 py-1 border rounded bg-gray-200 hover:bg-gray-300">
        &lt; Return to Search
      </button>

      <div className="flex items-center space-x-4 mb-4">
        <button onClick={() => setPriceAsc(!priceAsc)} className="border rounded px-3 py-1">
          Price {priceAsc ? '⬆' : '⬇'}
        </button>
        <button onClick={() => setDurAsc(!durAsc)} className="border rounded px-3 py-1">
          Duration {durAsc ? '⬆' : '⬇'}
        </button>
      </div>

      {slice.map(o => (
        <FlightCard key={o.id} offer={o} currencyCode={search.currencyCode as string} adults={search.adults as number} />
      ))}

      <Pagination currentPage={page} totalPages={totalPages} onPageChange={setPage} />
    </div>
  )
}