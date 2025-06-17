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

  const priced = offer;

  return (
    <div className="p-6">
      <button
      onClick={() => navigate(-1)}
      className="mb-6 inline-flex items-center text-sm text-blue-600 hover:underline"
      >  
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
}