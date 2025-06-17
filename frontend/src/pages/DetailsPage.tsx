/*import { useLocation, useNavigate } from 'react-router-dom';
import SegmentCard from '../components/SegmentCard';
import PriceSidebar from '../components/PriceSidebar';

export default function DetailsPage() {
  const { state } = useLocation() as {
    state: { offer: any; search: any };
  };

  const navigate = useNavigate();
  const offer  = state.offer;   
  const search = state.search;  

  const priced = offer;

  return (
    <div className="p-6">
      <button
      onClick={() => navigate(-1)}
      className="mb-6 inline-flex items-center text-sm text-blue-600 hover:underline checkButtonDetails"
      >Return 
      </button>
      <div className="md:col-span-2 space-y-6">
        {offer.itineraries.map((it: any, idx: number) =>
          it.segments.map((seg: any, sIdx: number) => (
            <SegmentCard
              key={`${idx}-${sIdx}`}
              index={sIdx + 1}
              segment={seg}
              priceInfo={priced}
              currencyCode={search.currencyCode}
            />
          ))
        )}
      </div>

      <PriceSidebar
        pricing={priced}
        adults={search.adults}
        currencyCode={search.currencyCode}
      />
    </div>
  );
}*/
// src/pages/DetailsPage.tsx
import { useLocation, useNavigate } from 'react-router-dom';
import SegmentCard from '../components/SegmentCard';
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
      >
        ← Back
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
                <SegmentCard
                  key={`${idx}-${sIdx}`}
                  index={sIdx + 1}
                  segment={seg}
                  priceInfo={offer}
                  currencyCode={search.currencyCode}
                />
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