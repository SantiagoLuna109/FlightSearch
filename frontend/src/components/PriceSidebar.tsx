type Props = {
    pricing: any
    adults: number
    currencyCode: string
  }
  
  export default function PriceSidebar({ pricing, adults, currencyCode }: Props) {
    const intl = new Intl.NumberFormat('en-US', { style: 'currency', currency: currencyCode })
    const fmt = (value: number) => `${intl.format(value)} ${currencyCode}`
    const base = Number(pricing.price.base)
    const total = Number(pricing.price.grandTotal)
    const fees = total - base;
    const per = total / adults
  
    return (
      <div className="border rounded p-4 space-y-4">
        <div>
          <p className="font-semibold mb-1">Price Breakdown</p>
          <p>Base&nbsp;&nbsp;{fmt(base)}</p>
          <p>Fees&nbsp;&nbsp;{fmt(fees)}</p>
          <p className="font-bold mt-2">Total&nbsp;&nbsp;{fmt(total)}</p>
        </div>
  
        <div className="border p-3">
          <p className="font-semibold mb-1">Per Traveler</p>
          <p>{fmt(per)}</p>
        </div>
      </div>
    )
  }