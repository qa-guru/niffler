import {FC, ReactNode, createContext, useContext } from "react";
import { Country } from "../types/Country";

type CountriesContextData = {
    countries: Country[];
};

const CountriesContext = createContext({} as CountriesContextData);

interface CountriesContextProviderProps {
    children: ReactNode;
}
const CountriesProvider: FC<CountriesContextProviderProps> = ({children}) => {
    const {data} = {};

    return (
        <CountriesContext.Provider value={{ countries: data?.countries ?? [] }}>
            {children}
        </CountriesContext.Provider>
    );
};

const useCountries = (): CountriesContextData => {
    const context = useContext(CountriesContext);

    if (!context) {
        throw new Error('useCountries must be used within an CountriesProvider');
    }

    return context;
};

export { CountriesProvider, useCountries };