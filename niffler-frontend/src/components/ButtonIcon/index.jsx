export const IconType = {
    ADD_FRIEND: "add",
    EDIT: "edit",
    CLOSE: "close",
    LOGOUT: "logout",
    SUBMIT: "submit",
    MESSAGE: "message",
    MESSAGE_NEW: "message-new",
};

export const ButtonIcon = ({iconType, onClick, text}) => {

    return (
        <button className={`button-icon button-icon_type_${iconType}`} type={"button"} onClick={onClick}>
            <span className={"button-icon__text"}>{text}</span>
        </button>
    );
}
