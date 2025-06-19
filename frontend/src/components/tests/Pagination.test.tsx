import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import Pagination from '../Pagination';

describe('<Pagination>', () => {
  const setup = (page = 0, total = 5) => {
    const spy = jest.fn();
    render(<Pagination currentPage={page} totalPages={total} onPageChange={spy} />);
    return spy;
  };

  it('disables « and ‹ on the first page', () => {
    setup(0, 5);
    expect(screen.getByRole('button', { name: '<<' })).toBeDisabled();
    expect(screen.getByRole('button', { name: '<' })).toBeDisabled();
  });

  it('bold-faces the active page button', () => {
    setup(2, 5);                
    const btn = screen.getByRole('button', { name: '3' });
    expect(btn).toHaveStyle({ fontWeight: 'bold' });
  });

  it('calls onPageChange with the right index', async () => {
    const spy = setup(0, 5);
    await userEvent.click(screen.getByRole('button', { name: '2' }));
    expect(spy).toHaveBeenCalledWith(1);            
  });
});
