import { useForm } from 'react-hook-form'
import { useMutation } from '@tanstack/react-query'
import axios from 'axios'
import DatePicker from 'react-datepicker'
import 'react-datepicker/dist/react-datepicker.css'
import AirportCombo from './AirportCombo'
import { useNavigate } from 'react-router-dom'

type Request = {
  originLocationCode: string
  destinationLocationCode: string
  departureDate: string
  returnDate?: string
  currency: 'USD' | 'MXN' | 'EUR'
  nonStop: boolean
  adults: number
}

export default function FlightSearchForm() {
  const {
    register,
    handleSubmit,
    setValue,
    watch,
    formState: { errors }
  } = useForm<Request>({
    defaultValues: { currency: 'USD', nonStop: false, adults: 1 }
  })

  const navigate = useNavigate()

  const search = useMutation({
    mutationFn: (payload: Request) =>
      axios.post('/api/v1/flights/search', payload).then(r => r.data),
    onSuccess: (response, variables) => {
      const offers = Array.isArray(response) ? response : response.data ?? []
      navigate('/results', { state: { offers, search: variables } })
    }
  })

  const date2str = (d: Date) => d.toISOString().slice(0, 10)
  const departure = watch('departureDate')
  const onSubmit = handleSubmit(d => search.mutate(d))

  return (
    <div className="max-w-xl w-full bg-white shadow-md rounded-lg p-6">
      <h2 className="text-2xl font-bold text-center mb-6">Flight Search</h2>

      <form onSubmit={onSubmit} className="grid gap-4 md:grid-cols-2">
        <AirportCombo
          label="From"
          value={watch('originLocationCode')}
          onChange={iata => setValue('originLocationCode', iata ?? '', { shouldValidate: true })}
        />

        <AirportCombo
          label="To"
          value={watch('destinationLocationCode')}
          onChange={iata => setValue('destinationLocationCode', iata ?? '', { shouldValidate: true })}
        />

        <div className="flex flex-col">
          <label className="font-semibold mb-1">Departure date</label>
          <DatePicker
            className="border rounded p-2 w-full"
            minDate={new Date()}
            selected={departure ? new Date(departure) : null}
            onChange={d => setValue('departureDate', date2str(d as Date), { shouldValidate: true })}
            dateFormat="yyyy-MM-dd"
          />
          {errors.departureDate && <small className="text-red-600">Required</small>}
        </div>

        <div className="flex flex-col">
          <label className="font-semibold mb-1">Return date</label>
          <DatePicker
            className="border rounded p-2 w-full"
            minDate={departure ? new Date(departure) : new Date()}
            selected={watch('returnDate') ? new Date(watch('returnDate')!) : null}
            onChange={d => setValue('returnDate', d ? date2str(d as Date) : undefined)}
            dateFormat="yyyy-MM-dd"
            isClearable
          />
        </div>

        <div className="flex flex-col">
          <label className="font-semibold mb-1">Currency</label>
          <select {...register('currency')} className="border rounded p-2">
            <option>USD</option>
            <option>MXN</option>
            <option>EUR</option>
          </select>
        </div>

        <div className="flex flex-col">
          <label className="font-semibold mb-1">Adults</label>
          <input
            type="number"
            min={1}
            {...register('adults', { required: true, min: 1 })}
            className="border rounded p-2"
          />
          {errors.adults && <small className="text-red-600">Required</small>}
        </div>

        <label className="flex items-center space-x-2 md:col-span-2">
          <input type="checkbox" {...register('nonStop')} />
          <span className="font-semibold">Non-stop only</span>
        </label>

        <button
          type="submit"
          className="md:col-span-2 bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition disabled:opacity-60"
          disabled={search.isPending}
        >
          {search.isPending ? 'Searching…' : 'Search'}
        </button>
      </form>

      {search.isError && (
        <p className="mt-6 text-red-700 font-medium">Something went wrong – {String(search.error)}</p>
      )}
    </div>
  )
}