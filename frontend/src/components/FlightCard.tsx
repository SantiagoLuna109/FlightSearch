import { format, parseISO, differenceInMinutes } from 'date-fns';
import clsx from 'clsx';                 

type Props = {
  offer: any;          
  currency: string;     
};

function iso(t: string) { return parseISO(t); }

export default function FlightCard({ offer, currency }: Props) {
  const intl = new Intl.NumberFormat('en-US', { style: 'currency', currency }); 

  const [outbound, inbound] = offer.itineraries;
  const segmentsOut = outbound.segments;
  const segmentsBack = inbound?.segments ?? [];

  const dur = (it: any) => it.duration.replace('PT', '').toLowerCase(); 
  const stops = (it: any) =>
    it.segments.length - 1 ? `${it.segments.length - 1} stop` + (it.segments.length - 1 > 1 ? 's' : '') : 'Nonstop';

  return (
    <div className="border rounded shadow-sm bg-white overflow-hidden">
      {/* ----- Un saludote ----- */}
      <div className="grid grid-cols-4 p-4 gap-2">
        <div className="col-span-3 space-y-1">
          <p className="font-semibold">
            {format(iso(segmentsOut[0].departure.at), 'h:mmaaa')} – 
            {format(iso(segmentsOut.slice(-1)[0].arrival.at), 'h:mmaaa')}
          </p>
          <p>
            {segmentsOut[0].departure.iataCode} → {segmentsOut.slice(-1)[0].arrival.iataCode}{' '}
            ({stops(outbound)})
          </p>
          <p className="text-sm text-gray-600">{dur(outbound)}</p>
          <p className="text-sm mt-2">{offer.validatingAirlineCodes[0]}</p>
        </div>

        {/* Price strip */}
        <div className="col-span-1 flex flex-col items-end justify-center border-l pl-3">
          <p className="font-bold">{intl.format(Number(offer.price.grandTotal))}</p>
          <p className="text-sm text-gray-600">total</p>
          <p className="mt-1">{intl.format(Number(offer.travelerPricings[0].price.total))}</p>
          <p className="text-sm text-gray-600">per traveler</p>
        </div>
      </div>

      {/* ----- inbound shown only in round-trip salu2 ----- */}
      {segmentsBack.length > 0 && (
        <>
          <hr />
          <div className="grid grid-cols-4 p-4 gap-2">
            <div className="col-span-3 space-y-1">
              <p className="font-semibold">
                {format(iso(segmentsBack[0].departure.at), 'h:mmaaa')} – 
                {format(iso(segmentsBack.slice(-1)[0].arrival.at), 'h:mmaaa')}
              </p>
              <p>
                {segmentsBack[0].departure.iataCode} → {segmentsBack.slice(-1)[0].arrival.iataCode}{' '}
                ({stops(inbound)})
              </p>
              <p className="text-sm text-gray-600">{dur(inbound)}</p>
              <p className="text-sm mt-2">{offer.validatingAirlineCodes[0]}</p>
            </div>
          </div>
        </>
      )}
    </div>
  );
}