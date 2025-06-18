import { useLocation, useNavigate } from 'react-router-dom';
import SegmentCard from '../components/SegmentCard';
import StopCard from '../components/StopCard';
import PriceSidebar from '../components/PriceSidebar';

export default function DetailsPage() {
  const { state } = useLocation() as {
    state: { offer: any; search: any };
  };
  const navigate = useNavigate();
  const offer  = state.offer;
  const search = state.search;

  return (
    <div className="p-6">
      <button
        onClick={() => navigate(-1)}
        className="mb-6 inline-flex items-center text-sm text-blue-600 hover:underline"
      >Back
      </button>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
        <div className="md:col-span-2 space-y-8">
          {offer.itineraries.map((it: any, idx: number) => (
            <div key={idx} className="space-y-6">
              {offer.itineraries.length > 1 && (
                <p className="text-lg font-semibold">
                  {idx === 0 ? 'Outbound' : 'Return'}
                </p>
              )}

              {it.segments.map((seg: any, sIdx: number) => (
                <div key={`${idx}-${sIdx}`} className="checkDetails"> 
                <SegmentCard
                  index={sIdx + 1}
                  segment={seg}
                  priceInfo={offer}
                  currencyCode={search.currencyCode}
                />
                {sIdx < it.segments.length - 1 && (
                  <StopCard
                    prevArr={seg.arrival}
                    nextDep={it.segments[sIdx + 1].departure}
                  />
                )}
              </div>
              
              ))}
            </div>
          ))}
        </div>

        <PriceSidebar
          pricing={offer}
          adults={search.adults}
          currencyCode={search.currencyCode}
        />
      </div>
    </div>
  );
}