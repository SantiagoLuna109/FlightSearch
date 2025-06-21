import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import FlightCard from '../FlightCard';

const fakeOffer = {
  id: 'OFF-1',
  price: { grandTotal: '500.00' },
  itineraries: [
    {
      duration: 'PT2H',
      segments: [
        {
          departure: { at: '2025-06-23T10:00:00', iataCode: 'JFK', airportName: 'JFK' },
          arrival:   { at: '2025-06-23T12:00:00', iataCode: 'YYZ', airportName: 'YYZ' },
          carrierName: 'Delta',
          operating: { carrierCode: 'DL', carrierName: 'Delta' },
          flightNumber: '123'
        }
      ]
    }
  ],
  validatingAirlineCodes: ['DL']
};

describe('<FlightCard>', () => {
  it('shows price per traveller and total', () => {
    render(
      <MemoryRouter>
        <FlightCard offer={fakeOffer as any} currencyCode="USD" adults={2} />
      </MemoryRouter>
    );
    expect(screen.getByText(/\$500\.00 USD/)).toBeInTheDocument(); // total
    expect(screen.getByText(/\$250\.00 USD/)).toBeInTheDocument(); // per person
  });

  it('navigates on click', async () => {
    const { container } = render(
      <MemoryRouter>
        <FlightCard offer={fakeOffer as any} currencyCode="USD" adults={1} />
      </MemoryRouter>
    );
    await userEvent.click(container.firstChild as HTMLElement);
  });
});
