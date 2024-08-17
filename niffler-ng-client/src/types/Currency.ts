export interface Currency {
    currency: string,
    currencyRate?: number,
}

export type CurrencyValue = "RUB" | "KZT" | "USD" | "EUR" | "ALL";

export const convertCurrencyToData = (currency: Currency) => {
    switch (currency.currency) {
        case "RUB":
        case "KZT":
        case "EUR":
        case "USD":
            return currency.currency;
        default:
            return "";
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
