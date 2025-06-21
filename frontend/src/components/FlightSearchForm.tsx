import { useForm, Controller } from 'react-hook-form';
import { useMutation } from '@tanstack/react-query';
import axios from 'axios';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import AirportCombo from './AirportCombo';
import { useNavigate } from 'react-router-dom';

const LS_KEY = 'flightSearchCriteria';

type Request = {
  originLocationCode: string;
  destinationLocationCode: string;
  departureDate: string;
  returnDate?: string;
  currencyCode: 'USD' | 'MXN' | 'EUR';
  nonStop: boolean;
  adults: number;
};

export default function FlightSearchForm() {
  const saved: Partial<Request> | null = (() => {
    try {
      return JSON.parse(localStorage.getItem(LS_KEY) || 'null');
    } catch {
      return null;
    }
  })();

  const {
    control,
    register,
    handleSubmit,
    formState: { errors },
    watch, 
  } = useForm<Request>({
    defaultValues: {
      originLocationCode: '',
      destinationLocationCode: '',
      departureDate: '',
      returnDate: undefined,
      currencyCode: 'USD',
      nonStop: false,
      adults: 1,
      ...saved,
    },
  });

  const navigate = useNavigate();

  const date2str = (d: Date) =>
    `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(
      d.getDate()
    ).padStart(2, '0')}`;

  const search = useMutation({
    mutationFn: (payload: Request) =>
      axios.post('/api/v1/flights/search', payload).then((r) => r.data),
    onSuccess: (response, variables) => {
      localStorage.setItem(LS_KEY, JSON.stringify(variables));
      const offers = Array.isArray(response) ? response : response.data ?? [];
      navigate('/results', { state: { offers, search: variables } });
    },
  });

  const onSubmit = handleSubmit((data) => search.mutate(data));

  const depDateStr = watch('departureDate');
  const depDateObj = depDateStr ? new Date(`${depDateStr}T00:00:00`) : new Date();

  return (
    <div className="max-w-xl w-full bg-white shadow-md rounded-lg p-6 checkSearchForm">
      <h2 className="text-2xl font-bold text-center mb-6">Flight Search</h2>

      <form onSubmit={onSubmit} className="grid gap-4 md:grid-cols-2 checkSearchForm">
        
        <Controller
          name="originLocationCode"
          control={control}
          rules={{ required: 'Origin is required' }}
          render={({ field }) => (
            <div className="flex flex-col checkSearchForm">
              <AirportCombo label="From" value={field.value} onChange={(iata) => field.onChange(iata ?? '')} />
              {errors.originLocationCode && (
                <small className="text-red-600">{errors.originLocationCode.message}</small>
              )}
            </div>
          )}
        />

        <Controller
          name="destinationLocationCode"
          control={control}
          rules={{ required: 'Destination is required' }}
          render={({ field }) => (
            <div className="flex flex-col checkSearchForm">
              <AirportCombo label="To" value={field.value} onChange={(iata) => field.onChange(iata ?? '')} />
              {errors.destinationLocationCode && (
                <small className="text-red-600">{errors.destinationLocationCode.message}</small>
              )}
            </div>
          )}
        />

        <Controller
          name="departureDate"
          control={control}
          rules={{ required: 'Departure date is required' }}
          render={({ field }) => (
            <div className="flex flex-col checkSearchForm">
              <label className="font-semibold mb-1 ">Departure date</label>
              <DatePicker
                className="border rounded p-2 w-full "
                selected={field.value ? new Date(`${field.value}T00:00:00`) : null}
                onChange={(d) => field.onChange(date2str(d as Date))}
                dateFormat="yyyy-MM-dd"
                minDate={new Date()} 
              />
              {errors.departureDate && (
                <small className="text-red-600">{errors.departureDate.message}</small>
              )}
            </div>
          )}
        />

        <Controller
          name="returnDate"
          control={control}
          render={({ field }) => (
            <div className="flex flex-col checkSearchForm">
              <label className="font-semibold mb-1">Return date</label>
              <DatePicker
                className="border rounded p-2 w-full"
                selected={field.value ? new Date(`${field.value}T00:00:00`) : null}
                onChange={(d) => field.onChange(d ? date2str(d as Date) : undefined)}
                dateFormat="yyyy-MM-dd"
                isClearable
                minDate={depDateObj}
              />
            </div>
          )}
        />

        <div className="flex flex-col checkSearchForm">
          <label className="font-semibold mb-1">Currency</label>
          <select {...register('currencyCode')} className="border rounded p-2">
            <option>USD</option>
            <option>MXN</option>
            <option>EUR</option>
          </select>
        </div>

        <div className="flex flex-col checkSearchForm">
          <label className="font-semibold mb-1">Adults</label>
          <input
            type="number"
            min={1}
            {...register('adults', { required: true, min: 1 })}
            className="border rounded p-2"
          />
          {errors.adults && <small className="text-red-600">At least one adult</small>}
        </div>

        <label className="flex items-center space-x-2 md:col-span-2">
          <input type="checkbox" {...register('nonStop')} />
          <span className="font-semibold">Non-stop only</span>
        </label>

        <button
          type="submit"
          className="md:col-span-2 bg-blue-600 text-white py-2 rounded hover:rgb-02204A-700 transition disabled:opacity-60 searchButton"
          disabled={search.isPending}
        >
          {search.isPending ? 'Searching…' : 'Search'}
        </button>
      </form>

      {search.isError && (
        <p className="mt-6 text-red-700 font-medium">Something went wrong – {String(search.error)}</p>
      )}
    </div>
  );
}