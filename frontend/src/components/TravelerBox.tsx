type Props = {
    segmentId: string
    priceInfo: any
    currency: string
    intl: Intl.NumberFormat
  }
  
  export default function TravelerBox({ segmentId, priceInfo, intl }: Props) {
    const fares =
      priceInfo?.travelerPricings?.map((tp: any) =>
        tp.fareDetailsBySegment.find((f: any) => f.segmentId === segmentId)
      ) || []
  
    return (
      <div className="border p-2 h-full text-sm">
        <p className="font-semibold mb-1">Traveler fare details</p>
        {fares.map((f: any, i: number) => (
          <div key={i} className="mb-1">
            <p>
              {f.cabin} / {f.class}
            </p>
            {f.amenities?.map((a: any, j: number) => (
              <p key={j}>
                {a.description} {a.isChargeable ? intl.format(a.amount) : '(free)'}
              </p>
            ))}
          </div>
        ))}
      </div>
    )
  }