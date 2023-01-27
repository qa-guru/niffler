export const Button = ({type, buttonText = "", onClick, small, disabled= false}) => {

    return (
        <button className={`button ${small ? "button_type_small" : ""}`} type={type} onClick={onClick} disabled={disabled}>
            {buttonText}
        </button>
    );
}
