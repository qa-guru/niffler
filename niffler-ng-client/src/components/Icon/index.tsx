import addFriendIcon from "../../assets/icons/ic_add_friend.svg";
import checkIcon from "../../assets/icons/ic_check.svg";
import crossIcon from "../../assets/icons/ic_cross.svg";
import plusIcon from "../../assets/icons/ic_plus.svg";
import userIcon from "../../assets/icons/ic_user.svg";
import friendsIcon from "../../assets/icons/ic_friends.svg";
import allIcon from "../../assets/icons/ic_all.svg";
import signOutIcon from "../../assets/icons/ic_signout.svg";
import editIcon from "../../assets/icons/ic_edit.svg";
import archiveIcon from "../../assets/icons/ic_archive.svg";
import uploadIcon from "../../assets/icons/ic_upload.svg";


import {FC} from "react";

export type IconType = "addFriendIcon" | "checkIcon" | "crossIcon" | "plusIcon" | "userIcon" | "friendsIcon" | "allIcon"| "signOutIcon" | "editIcon" | "archiveIcon" | "uploadIcon";

export interface IconProps {
    type: IconType,
}

export const Icon: FC<IconProps> = ({type}) => <img src={getIcon(type)} alt="" style={{color: '#A8ACC0',}}/>

const getIcon = (type: IconType): string => {
    switch(type) {
        case "addFriendIcon":
            return addFriendIcon;
        case "checkIcon":
            return checkIcon;
        case "crossIcon":
            return crossIcon;
        case "plusIcon":
            return plusIcon;
        case "userIcon":
            return userIcon;
        case "friendsIcon":
            return friendsIcon;
        case "allIcon":
            return allIcon;
        case "signOutIcon":
            return signOutIcon;
        case "editIcon":
            return editIcon;
        case "archiveIcon":
            return archiveIcon;
        case "uploadIcon":
            return uploadIcon;
    }
}