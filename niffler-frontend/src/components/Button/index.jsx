export const Button = ({type, buttonText = "", onClick, small, disabled= false, selected = false}) => {

    return (
        <button className={`button ${small ? "button_type_small" : ""} ${selected ? "button_type_selected" : ""}`}
                type={type}
                onClick={onClick}
                disabled={disabled}>
            {buttonText}
        </button>
    );
}
