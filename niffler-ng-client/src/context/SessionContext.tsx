import {createContext, Dispatch, SetStateAction} from "react";
import { User } from "../types/User";

interface SessionContextInterface {
    user: User;
    updateUser: Dispatch<SetStateAction<User>>;
}

export const USER_INITIAL_STATE: User = {
    id: "",
    username: "",
    fullname: "",
    photo: "",
    photoSmall: "",
}

const defaultState = {
    updateUser: () => {},
    user: USER_INITIAL_STATE,
};

export const SessionContext = createContext<SessionContextInterface>(defaultState);

