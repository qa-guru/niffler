import {createContext} from "react";

export const CurrencyContext = createContext({
    selectedCurrency: null,
    setSelectedCurrency: () => {},
});
