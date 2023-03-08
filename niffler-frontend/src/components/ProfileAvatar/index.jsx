import {useEffect, useState} from "react";
import {ButtonIcon, IconType} from "../ButtonIcon";

export const ProfileAvatar = ({username, value, handleChangeValue}) => {
    const [isEditAvatar, setIsEditAvatar] = useState(false);
    const [error, setError] = useState(null);
    useEffect(() => {
        setIsEditAvatar(false);
    }, [value]);

    const editAvatarView = (

        <div className="edit-avatar__container">
            <span className="edit-avatar__close">
                <ButtonIcon iconType={IconType.CLOSE} onClick={() => {
                    setIsEditAvatar(!isEditAvatar);
                    setError(null);
                }}/>
            </span>
                <p className="edit-avatar__header">Update Avatar</p>
                <input className="edit-avatar__input"
                       type="file"
                       name="avatar"
                       accept="image/png, image/jpeg"
                       onChange={(e) => {
                           const selectedFile = e.target.files[0];
                           if (selectedFile.size > 3145728 ) {
                               e.target.value = null;
                               setError("Maximum file length is 3MB");
                           } else {
                               handleChangeValue(selectedFile);
                               setError(null);
                           }
                       }} required/>
        </div>
    );

    const contentImage = (
        <div className="edit-avatar__container">
            <button className="profile__avatar-edit" onClick={() => {
                setIsEditAvatar(!isEditAvatar);
                setError(null);
            }}>
                <img className="profile__avatar"
                     src={ value ?? "/images/niffler_avatar.jpeg"} alt="Аватар профиля"
                     width={250} height={250}/>
            </button>
        </div>
    );

    return (
        <div>
            <figure className={"avatar-container"}>
                {isEditAvatar ? editAvatarView : contentImage}
                {error && <span className="edit-avatar__error">{error}</span>}
                <figcaption>{username}</figcaption>
            </figure>
        </div>
    );
}
