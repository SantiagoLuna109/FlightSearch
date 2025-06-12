import { useQuery } from '@tanstack/react-query';
import axios from 'axios';
import { useDebounce } from '../utils/useDebounce';

export type Airport = {
  code: string;     
  name: string;     
  city: string;     
  country: string;  
};

export function useAirportSearch(keyword: string) {
  const debounced = useDebounce(keyword, 400);

  return useQuery({
    queryKey: ['airports', debounced],
    queryFn: async () => {
      if (debounced.length < 2) return [];

      const res = await axios.get('/api/v1/airports/search', {
        params: { keyword: debounced }
      });
      const raw = Array.isArray(res.data) ? res.data : res.data.data ?? [];
      return raw.map((item: any) => ({
        code:    item.iataCode,
        name:    item.name,
        city:    item.address?.cityName    ?? '',
        country: item.address?.countryName ?? ''
      })) as Airport[];
    },
    staleTime: 1000 * 60 * 5        
  });
}