import {createContext} from "react";

export const PopupContext = createContext({
    isOpen: false,
    setIsOpen: () => {},
});
