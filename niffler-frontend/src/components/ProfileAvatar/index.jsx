import {useState} from "react";
import {Currency as IconType} from "../../constants/iconType";
import {ButtonIcon} from "../ButtonIcon";

export const ProfileAvatar = ({username, value, handleChangeValue}) => {
    const [isEditAvatar, setIsEditAvatar] = useState(false);

    const editAvatarView = (

        <div className="edit-avatar__container">
            <span className="edit-avatar__close">
                <ButtonIcon iconType={IconType.CLOSE} onClick={() => setIsEditAvatar(!isEditAvatar)}/>
            </span>
                <p className="edit-avatar__header">Update Avatar</p>
                <input className="edit-avatar__input" type="file" name="avatar" accept="image/png, image/jpeg"  onChange={handleChangeValue} required/>
                <span className="edit-avatar__error"></span>
        </div>
    );

    const contentImage = (
        <div className="edit-avatar__container">
            <button className="profile__avatar-edit" onClick={() => {
                setIsEditAvatar(!isEditAvatar)
            }}>
                <img className="profile__avatar"
                     src={ "/images/niffler_avatar.jpeg"} alt="Аватар профиля"
                     width={150} height={150}/>
            </button>
        </div>
    );

    return (
        <div>
            <figure className={"avatar-container"}>
                {isEditAvatar ? editAvatarView : contentImage}
                <figcaption>{username}</figcaption>
            </figure>
        </div>
    );
}
