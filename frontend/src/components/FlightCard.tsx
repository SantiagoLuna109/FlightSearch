import { format, parseISO } from 'date-fns'

type Props = { offer: any; currency: string }

const iso = (t: string) => parseISO(t)
const dur = (it: any) => it.duration.replace('PT', '').toLowerCase()
const stops = (it: any) => {
  const n = it.segments.length - 1
  return n === 0 ? 'Non-stop' : `${n} stop${n > 1 ? 's' : ''}`
}

export default function FlightCard({ offer, currency }: Props) {
  const intl = new Intl.NumberFormat('en-US', { style: 'currency', currency })
  const [outbound, inbound] = offer.itineraries
  const segOut = outbound.segments
  const segBack = inbound?.segments ?? []
  const adults = offer.travelerPricings?.length ?? 1
  const per = Number(offer.travelerPricings?.[0]?.price?.total ?? 0)
  const tot = Number(offer.price.grandTotal)

  return (
    <div className="border rounded shadow-sm bg-white overflow-hidden">
      <div className="grid grid-cols-4 p-4 gap-2">
        <div className="col-span-3 space-y-1">
          <p className="font-semibold">
            {format(iso(segOut[0].departure.at), 'h:mmaaa')} – {format(iso(segOut.at(-1)!.arrival.at), 'h:mmaaa')}
          </p>
          <p>
            {segOut[0].departure.iataCode} → {segOut.at(-1)!.arrival.iataCode} ({stops(outbound)})
          </p>
          <p className="text-sm text-gray-600">{dur(outbound)}</p>
          <p className="text-sm mt-2">{offer.validatingAirlineCodes[0]}</p>
        </div>
        <div className="col-span-1 flex flex-col items-end justify-center border-l pl-3">
          <p className="font-bold">{intl.format(tot)}</p>
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
                {format(iso(segBack[0].departure.at), 'h:mmaaa')} – {format(iso(segBack.at(-1)!.arrival.at), 'h:mmaaa')}
              </p>
              <p>
                {segBack[0].departure.iataCode} → {segBack.at(-1)!.arrival.iataCode} ({stops(inbound)})
              </p>
              <p className="text-sm text-gray-600">{dur(inbound)}</p>
              <p className="text-sm mt-2">{offer.validatingAirlineCodes[0]}</p>
            </div>
          </div>
        </>
      )}
    </div>
  )
}