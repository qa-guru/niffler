import {Category as FullCategory, CurrentUserQuery} from "../generated/graphql.tsx";

export type Categories = CurrentUserQuery["user"]["categories"];
export type Category = Omit<FullCategory, "username">;
