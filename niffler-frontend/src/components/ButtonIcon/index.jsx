export const IconType = {
    EDIT: "edit",
    CLOSE: "close",
    LOGOUT: "logout",
    SUBMIT: "submit",
};

export const ButtonIcon = ({iconType, onClick, text}) => {

    return (
        <button className={`button-icon button-icon_type_${iconType}`} type={"button"} onClick={onClick}>
            <span className={"button-icon__text"}>{text}</span>
        </button>
    );
}
