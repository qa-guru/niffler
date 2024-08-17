import {createContext, Dispatch, SetStateAction} from "react";
import {User} from "../types/User";
import {Statistic} from "../types/Statistic.ts";

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

export const STAT_INITIAL_STATE: Statistic = {
    total: 0.0,
    currency: "RUB",
    statByCategories: [],
}

const defaultState = {
    updateUser: () => {
    },
    user: USER_INITIAL_STATE,
    statistic: STAT_INITIAL_STATE,
};

export const SessionContext = createContext<SessionContextInterface>(defaultState);
