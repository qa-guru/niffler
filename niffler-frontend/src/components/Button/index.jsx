export const Button = ({type, buttonText = "", onClick, small}) => {

    return (
        <button className={`button ${small ? "button_type_small" : ""}`} type={type} onClick={onClick}>
            {buttonText}
        </button>
    );
}
