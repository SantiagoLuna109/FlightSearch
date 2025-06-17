import { format, parseISO } from 'date-fns'
import TravelerBox from './TravelerBox'

type Amenity = {
  description: string
  isChargeable: boolean
}

type FareDetail = {
  travelerId: string
  cabin: string
  fareClass: string
  amenities: Amenity[]
}

type Props = {
  index: number
  segment: any
  priceInfo: any
  currencyCode: string
}

export default function SegmentCard({
  index,
  segment,
  priceInfo,
  currencyCode,
}: Props) {
  const intl = new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: currencyCode,
  })

  const dep = segment.departure
  const arr = segment.arrival
  const op = segment.operating
  const fares: FareDetail[] = segment.fareDetailsBySegment || []

  return (
    <div className="border rounded p-4 grid md:grid-cols-4 gap-4">
      <div className="md:col-span-3 space-y-2">
        <p className="font-semibold">Segment {index}</p>

        <p>
          {format(parseISO(dep.at), 'yyyy-MM-dd HH:mm')} –{' '}
          {format(parseISO(arr.at), 'yyyy-MM-dd HH:mm')}
        </p>

        <p>
          {dep.airportName || dep.iataCode} ({dep.iataCode}) →{' '}
          {arr.airportName || arr.iataCode} ({arr.iataCode})
        </p>

        <p>
          <strong>{segment.carrierCode}</strong>{' '}
          <span className="text-sm text-gray-600">
            {segment.carrierName}
          </span>
        </p>

        {op &&
          op.carrierCode &&
          op.carrierCode !== segment.carrierCode && (
            <p className="text-sm text-gray-600">
              Operated by <strong>{op.carrierCode}</strong>{' '}
              <span>{op.carrierName}</span>
            </p>
          )}

        <p className="text-sm">
          {segment.aircraft.name || segment.aircraft.code}
        </p>

        {fares.length > 0 && (
          <div className="pt-2">
            <p className="font-medium">Traveler Fare Details:</p>
            {fares.map((fd, i) => (
              <div key={i} className="pl-4 py-1">
                <p className="text-sm">
                  <strong>Traveler {fd.travelerId}:</strong>{' '}
                  {fd.cabin} / {fd.fareClass}
                </p>
                <ul className="list-disc list-inside text-sm">
                  {fd.amenities.map((am, ai) => (
                    <li key={ai}>
                      {am.description}{' '}
                      {am.isChargeable ? (
                        <span className="text-xs text-red-600">(Paid)</span>
                      ) : (
                        <span className="text-xs text-green-600">
                          (Complimentary)
                        </span>
                      )}
                    </li>
                  ))}
                </ul>
              </div>
            ))}
          </div>
        )}
      </div>

      <TravelerBox
        segmentId={segment.id}
        priceInfo={priceInfo}
        currency={currencyCode}
        intl={intl}
      />
    </div>
  )
}