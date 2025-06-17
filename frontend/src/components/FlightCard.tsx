import { format, parseISO } from 'date-fns'
import { useNavigate } from 'react-router-dom'

type Props = { offer: any; currencyCode: string; adults: number }

const iso = (t: string) => parseISO(t)
const dur = (it: any) => it.duration.replace('PT', '').toLowerCase()
const layovers = (segs: any[]) =>
  segs.length > 1
    ? segs
        .slice(1)
        .map((s: any, i: number) => {
          const prevArr = segs[i].arrival.at
          const diff =
            (new Date(s.departure.at).getTime() - new Date(prevArr).getTime()) /
            60000
          const h = Math.floor(diff / 60)
          const m = diff % 60
          const stop = segs[i].arrival
          return `${stop.airportName} (${stop.iataCode}) ${h}h${m ? ' ' + m + 'm' : ''}`
        })
        .join(', ')
    : 'Non-stop'

export default function FlightCard({ offer, currencyCode, adults }: Props) {
  const intl = new Intl.NumberFormat('en-US', { style: 'currency', currency: currencyCode })
  const [outbound, inbound] = offer.itineraries
  const segOut = outbound.segments
  const segBack = inbound?.segments ?? []
  const total = Number(offer.price.grandTotal)
  const per = total / adults
  const nav = useNavigate()
  const airlineCode = offer.validatingAirlineCodes[0]
  const airlineName = segOut[0].carrierName
  const op = segOut[0].operating?.carrierCode ?? ''
  const opName = segOut[0].operating?.carrierName ?? ''

  return (
    <div
      className="border rounded shadow-sm bg-white overflow-hidden cursor-pointer"
      onClick={() => nav(`/details/${offer.id}`, { state: { offer, search: { currencyCode, adults } } })}
    >
      <div className="grid grid-cols-4 p-4 gap-2">
        <div className="col-span-3 space-y-1">
          <p className="font-semibold">
            {format(iso(segOut[0].departure.at), 'MMM d, h:mmaaa')} – {format(iso(segOut.at(-1)!.arrival.at), 'MMM d, h:mmaaa')}
          </p>
          <p>
            {segOut[0].departure.airportName} ({segOut[0].departure.iataCode}) → {segOut.at(-1)!.arrival.airportName} ({segOut.at(-1)!.arrival.iataCode})
          </p>
          <p className="text-sm">
            {airlineName} {airlineCode}
            {op && op !== airlineCode && ` — operated by ${opName} ${op}`}
          </p>
          <p className="text-sm">{dur(outbound)}</p>
          <p className="text-xs text-gray-600">{layovers(segOut)}</p>
        </div>

        <div className="col-span-1 flex flex-col items-end justify-center border-l pl-3">
          <p className="font-bold">{intl.format(total)}</p>
          <p className="text-sm text-gray-600">total for {adults}</p>
          {adults > 1 && (
            <>
              <p className="mt-1">{intl.format(per)}</p>
              <p className="text-sm text-gray-600">per traveller</p>
            </>
          )}
        </div>
      </div>

      {segBack.length > 0 && (
        <>
          <hr />
          <div className="grid grid-cols-4 p-4 gap-2">
            <div className="col-span-3 space-y-1">
              <p className="font-semibold">
                {format(iso(segBack[0].departure.at), 'MMM d, h:mmaaa')} – {format(iso(segBack.at(-1)!.arrival.at), 'MMM d, h:mmaaa')}
              </p>
              <p>
                {segBack[0].departure.airportName} ({segBack[0].departure.iataCode}) → {segBack.at(-1)!.arrival.airportName} ({segBack.at(-1)!.arrival.iataCode})
              </p>
              <p className="text-sm">{dur(inbound)}</p>
              <p className="text-xs text-gray-600">{layovers(segBack)}</p>
            </div>
          </div>
        </>
      )}
    </div>
  )
}