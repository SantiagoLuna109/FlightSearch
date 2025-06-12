import { useForm } from 'react-hook-form';
import { useMutation } from '@tanstack/react-query';
import axios from 'axios';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import AirportCombo from './AirportCombo';

type Request = {
  originLocationCode: string;
  destinationLocationCode: string;
  departureDate: string;
  returnDate?: string;
  currency: 'USD' | 'MXN' | 'EUR';
  nonStop: boolean;
};

export default function FlightSearchForm() {
  const {
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors }
  } = useForm<Request>({
    defaultValues: { currency: 'USD', nonStop: false }
  });

  const search = useMutation({
    mutationFn: (payload: Request) =>
      axios.post('/api/v1/flights/search', payload).then((r) => r.data)
  });

  const date2str = (d: Date) => d.toISOString().slice(0, 10);
  const departure = watch('departureDate');

  const onSubmit = handleSubmit((data) => search.mutate(data));

  return (
    <div className="max-w-xl w-full bg-white shadow-md rounded-lg p-6">
      <h2 className="text-2xl font-bold text-center mb-6">Flight Search</h2>

      <form onSubmit={onSubmit} className="grid gap-4 md:grid-cols-2">
        {/* airport pickers */}
        <AirportCombo
          label="From"
          value={watch('originLocationCode')}
          onChange={(iata) =>
            setValue('originLocationCode', iata ?? '', { shouldValidate: true })
          }
        />

        <AirportCombo
          label="To"
          value={watch('destinationLocationCode')}
          onChange={(iata) =>
            setValue('destinationLocationCode', iata ?? '', { shouldValidate: true })
          }
        />

        {/* departure date */}
        <div className="flex flex-col">
          <label className="font-semibold mb-1">Departure date</label>
          <DatePicker
            className="border rounded p-2 w-full"
            minDate={new Date()}
            selected={departure ? new Date(departure) : null}
            onChange={(d: Date) =>
              setValue('departureDate', date2str(d), { shouldValidate: true })
            }
            dateFormat="yyyy-MM-dd"
          />
          {errors.departureDate && <small className="text-red-600">Required</small>}
        </div>

        {/* return date */}
        <div className="flex flex-col">
          <label className="font-semibold mb-1">Return date</label>
          <DatePicker
            className="border rounded p-2 w-full"
            minDate={departure ? new Date(departure) : new Date()}
            selected={watch('returnDate') ? new Date(watch('returnDate')!) : null}
            onChange={(d: Date | null) =>
              setValue('returnDate', d ? date2str(d) : undefined)
            }
            dateFormat="yyyy-MM-dd"
            isClearable
          />
        </div>

        {/* currency */}
        <div className="flex flex-col">
          <label className="font-semibold mb-1">Currency</label>
          <select {...register('currency')} className="border rounded p-2">
            <option>USD</option>
            <option>MXN</option>
            <option>EUR</option>
          </select>
        </div>

        {/* non-stop */}
        <label className="flex items-center space-x-2 md:col-span-2">
          <input type="checkbox" {...register('nonStop')} />
          <span className="font-semibold">Non-stop only</span>
        </label>

        <button
          type="submit"
          className="md:col-span-2 bg-blue-600 text-white py-2 rounded
                     hover:bg-blue-700 transition disabled:opacity-60"
          disabled={search.isPending}>
          {search.isPending ? 'Searching…' : 'Search'}
        </button>
      </form>

      {/* result preview */}
      {search.isSuccess && (
        <pre className="mt-6 bg-slate-100 p-4 overflow-x-auto rounded max-h-96">
          {JSON.stringify(search.data, null, 2)}
        </pre>
      )}
      {search.isError && (
        <p className="mt-6 text-red-700 font-medium">
          Something went wrong – {String(search.error)}
        </p>
      )}
    </div>
  );
}