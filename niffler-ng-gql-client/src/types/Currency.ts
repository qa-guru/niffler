import {CurrencyValues} from "../generated/graphql.tsx";

export interface Currency {
    currency: string,
    currencyRate?: number,
}

export type CurrencyValue = "RUB" | "KZT" | "USD" | "EUR" | "ALL";

export const convertCurrencyToData = (currency: Currency) => {
    switch (currency.currency) {
        case "RUB":
            return CurrencyValues.Rub;
        case "KZT":
            return CurrencyValues.Kzt;
        case "EUR":
            return CurrencyValues.Eur;
        case "USD":
            return CurrencyValues.Usd
        default:
            return;
    }
}

export const getCurrencyIcon = (currency: CurrencyValue) => {
    switch (currency) {
        case "KZT":
            return "₸";
        case "RUB":
            return "₽";
        case "EUR":
            return "€";
        case "USD":
            return "$";
        case "ALL":
            return "⚖";
        default:
            return "";
    }
}
