import { FX_RATES } from "../utils/fxRates";

export function useFx(target: 'USD'|'MXN'|'EUR'){
    const toTarget = (amountUsd: number) => amountUsd * FX_RATES[target];

    const fmt = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: target
    });
}