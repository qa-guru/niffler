import {CurrentUserQuery, FriendshipStatus} from "../generated/graphql.tsx";

export type User = CurrentUserQuery["user"];


export type TableUser = {
    id: string;
    username: string;
    photoSmall?: string | null;
    fullname?: string | null;
    friendshipStatus?: FriendshipStatus | null | undefined;
}
