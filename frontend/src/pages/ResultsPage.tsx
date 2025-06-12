import { useLocation, Link } from 'react-router-dom';
import { format, parseISO, differenceInMinutes } from 'date-fns'; 
import FlightCard from '../components/FlightCard';

export default function ResultsPage() {
  // router state = { offers, search }
  const location = useLocation() as {
    state: { offers: any[]; search: Record<string, unknown> };
  };

  const { offers, search } = location.state ?? { offers: [], search: {} };

  if (!offers?.length) {
    return (
      <div className="p-6 text-center">
        <p className="mb-4">No offers returned. Try another search.</p>
        <Link to="/" className="text-blue-600 underline">Return to search</Link>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50 p-6 space-y-6 max-w-3xl mx-auto">
      <button
        onClick={() => window.history.back()}
        className="mb-4 px-3 py-1 border rounded bg-gray-200 hover:bg-gray-300"
      >
        &lt; Return to Search
      </button>

      {offers.map((offer) => (
        <FlightCard key={offer.id} offer={offer} currency={search.currency as string} />
      ))}
    </div>
  );
}