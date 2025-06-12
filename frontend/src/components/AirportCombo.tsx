// src/components/AirportCombo.tsx
import { Combobox } from '@headlessui/react';
import { useState } from 'react';
import { useAirportSearch } from '../hooks/useAirportSearch';
import type { Airport } from '../hooks/useAirportSearch';

type Props = {
  label: string;
  value: string | null;                    
  onChange: (iata: string | null) => void; 
};

export default function AirportCombo({ label, value, onChange }: Props) {
  const [query, setQuery]   = useState('');           
  const [selected, setSel]  = useState<Airport|null>(null); 
  const { data: airports = [], isLoading } = useAirportSearch(query);

  function handleSelect(a?: Airport | null) {
    setSel(a ?? null);          
    onChange(a?.code ?? null);  
    setQuery('');               
  }

  return (
    <Combobox value={selected} onChange={handleSelect} nullable>
      <div className="flex flex-col">
        <Combobox.Label className="font-semibold mb-1">{label}</Combobox.Label>

        {/* ------- INPUT ------- */}
        <div className="relative">
          <Combobox.Input
            className="w-full border rounded p-2"
            placeholder="Type a city or airport…"
            displayValue={(a: Airport) => (a ? `${a.city} (${a.code})` : '')}
            onChange={(e) => setQuery(e.target.value)}
          />

          {isLoading && (
            <span className="absolute right-3 top-3 animate-spin">⏳</span>
          )}

          {/* ------- DROPDOWN ------- */}
          <Combobox.Options
            className="absolute z-10 mt-1 max-h-60 w-full overflow-auto
                       rounded bg-white shadow-lg ring-1 ring-black/5">
            {airports.length === 0 && query !== '' && (
              <div className="p-2 text-gray-500">No matches</div>
            )}

            {airports.map((air) => (
              <Combobox.Option
                key={`${air.code}-${air.name}` /* unique key */}
                value={air}
                className={({ active }) =>
                  `cursor-pointer select-none p-2 ${
                    active ? 'bg-blue-100' : ''
                  }`
                }>
                {air.city}, {air.country} — {air.name}{' '}
                <span className="text-gray-500">({air.code})</span>
              </Combobox.Option>
            ))}
          </Combobox.Options>
        </div>
      </div>
    </Combobox>
  );
}