export const IconType = {
    EDIT: "edit",
    CLOSE: "close",
    LOGOUT: "logout",
};

export const ButtonIcon = ({iconType, onClick}) => {

    return (
        <button className={`button-icon button-icon_type_${iconType}`} type={"button"} onClick={onClick}>
        </button>
    );
}
