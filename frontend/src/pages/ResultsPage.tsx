import { Link, useLocation } from 'react-router-dom'
import { useState, useMemo } from 'react'
import { parseISO, differenceInMinutes } from 'date-fns'
import FlightCard from '../components/FlightCard'

export default function ResultsPage() {
  const location = useLocation() as {
    state: { offers: any[]; search: Record<string, any> }
  }
  const { offers = [], search = {} } = location.state ?? {}

  const [priceAsc, setPriceAsc] = useState(true)
  const [durAsc, setDurAsc] = useState(true)

  const sorted = useMemo(() => {
    const priceVal = (o: any) => Number(o.price.grandTotal)
    const durVal = (o: any) =>
      o.itineraries.reduce((m: number, it: any) => {
        const s = parseISO(it.segments[0].departure.at).getTime()
        const e = parseISO(it.segments.at(-1)!.arrival.at).getTime()
        return m + differenceInMinutes(e, s)
      }, 0)

    return [...offers].sort((a, b) => {
      const p = priceAsc ? priceVal(a) - priceVal(b) : priceVal(b) - priceVal(a)
      if (p !== 0) return p
      const d = durAsc ? durVal(a) - durVal(b) : durVal(b) - durVal(a)
      return d
    })
  }, [offers, priceAsc, durAsc])

  if (!sorted.length)
    return (
      <div className="p-6 text-center">
        <p className="mb-4">No offers returned. Try another search.</p>
        <Link to="/" className="text-blue-600 underline">
          Return to search
        </Link>
      </div>
    )

  return (
    <div className="min-h-screen bg-slate-50 p-6 space-y-6 max-w-3xl mx-auto">
      <button
        onClick={() => window.history.back()}
        className="mb-4 px-3 py-1 border rounded bg-gray-200 hover:bg-gray-300"
      >
        &lt; Return to Search
      </button>

      <div className="flex items-center space-x-4 mb-4">
        <button onClick={() => setPriceAsc(!priceAsc)} className="border rounded px-3 py-1">
          Price {priceAsc ? '⬆︎' : '⬇︎'}
        </button>
        <button onClick={() => setDurAsc(!durAsc)} className="border rounded px-3 py-1">
          Duration {durAsc ? '⬆︎' : '⬇︎'}
        </button>
      </div>

      {sorted.map(o => (
        <FlightCard
          key={o.id}
          offer={o}
          currency={search.currency as string}
          adults={search.adults as number}
        />
      ))}
    </div>
  )
}