import {createContext} from "react";

export const FilterContext = createContext({
    filter: null,
    setFilter: () => {},
});
