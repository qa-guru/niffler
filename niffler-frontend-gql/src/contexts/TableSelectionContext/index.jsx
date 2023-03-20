import {createContext} from "react";

export const TableSelectionContext = createContext({
    allIds: [],
    setAllIds: () => {
    },
    selectedIds: [],
    setSelectedIds: () => {
    },
});
