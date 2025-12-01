export interface User {
    id: string;
    username: string;
    fullname?: string;
    photo?: string;
    photoSmall?: string;
    friendshipStatus?: "FRIEND" | "INVITE_SENT" | "INVITE_RECEIVED";
}
