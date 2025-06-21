import FlightSearchForm from '../components/FlightSearchForm';

export default function SearchPage() {
  return (
    <div className="min-h-screen bg-slate-50 flex flex-col items-center py-8 px-4 checkSearch">
      <FlightSearchForm />
    </div>
  );
}