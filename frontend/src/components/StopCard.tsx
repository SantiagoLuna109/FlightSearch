import { differenceInMinutes, format, parseISO } from 'date-fns'

type Props = {
  prevArr: { airportName?: string; iataCode: string; at: string }
  nextDep: { airportName?: string; iataCode: string; at: string }
}

export default function StopCard({ prevArr, nextDep }: Props) {
  const minutes = differenceInMinutes(parseISO(nextDep.at), parseISO(prevArr.at))
  const h = Math.floor(minutes / 60)
  const m = minutes % 60
  return (
    <div className="border-l-4 border-gray-400 bg-black-50 p-4 rounded checkDetails">
      <p className="font-medium">
        Stop-over at {prevArr.airportName || prevArr.iataCode} ({prevArr.iataCode})
      </p>
      <p className="text-sm text-white-700">
        {h > 0 && `${h}h`} {m > 0 && `${m}m`} lay-over
        {' • '}
        Departure at {format(parseISO(nextDep.at), 'HH:mm')}
      </p>
    </div>
  )
}