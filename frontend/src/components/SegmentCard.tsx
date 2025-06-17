import { format, parseISO } from 'date-fns'
import TravelerBox from './TravelerBox'

type Props = {
  index: number
  segment: any
  priceInfo: any
  currencyCode: string
}

export default function SegmentCard({ index, segment, priceInfo, currencyCode }: Props) {
  const intl = new Intl.NumberFormat('en-US', { style: 'currency', currency: currencyCode })
  const dep = segment.departure
  const arr = segment.arrival
  const op = segment.operating

  return (
    <div className="border rounded p-4 grid md:grid-cols-4 gap-4">
      <div className="md:col-span-3 space-y-1">
        <p className="font-semibold">Segment {index}</p>
        <p>
          {format(parseISO(dep.at), 'yyyy-MM-dd HH:mm')} –{' '}
          {format(parseISO(arr.at), 'yyyy-MM-dd HH:mm')}
        </p>
        <p>
          {dep.iataCode} → {arr.iataCode}
        </p>
        <p>
          {segment.carrierCode} {segment.number}
        </p>
        {op && (
          <p className="text-sm text-gray-600">
            Operated by {op.carrierCode}
          </p>
        )}
        <p className="text-sm">{segment.aircraft.code}</p>
      </div>

      <TravelerBox
        segmentId={segment.id}
        priceInfo={priceInfo}
        currency={currencyCode} //check
        intl={intl}
      />
    </div>
  )
}
