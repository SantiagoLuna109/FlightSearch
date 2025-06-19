import { renderHook, act } from '@testing-library/react';
import { useDebounce } from '../useDebounce';

jest.useFakeTimers();

describe('useDebounce hook', () => {
  it('returns the initial value immediately', () => {
    const { result } = renderHook(() => useDebounce('Hello', 500));
    expect(result.current).toBe('Hello');
  });

  it('updates only after the given delay', () => {
    const { result, rerender } = renderHook(
      ({ text }) => useDebounce(text, 500),
      { initialProps: { text: 'A' } }
    );

    // trigger change
    rerender({ text: 'B' });
    expect(result.current).toBe('A');               // still old value
    act(() => { jest.advanceTimersByTime(499); });
    expect(result.current).toBe('A');               // still debouncing
    act(() => { jest.advanceTimersByTime(1); });
    expect(result.current).toBe('B');               // finally updated
  });
});
