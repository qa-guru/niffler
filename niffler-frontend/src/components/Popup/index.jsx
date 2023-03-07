import {useContext} from "react";
import {PopupContext} from "../../contexts/PopupContext";
import {Button} from "../Button";

export const Popup = () => {

    const {isOpen, setIsOpen} = useContext(PopupContext);

    return (
        isOpen ? (
            <div className="popup">
                <div className="popup__content">

                    <Button buttonText="Close" onClick={() => setIsOpen(false)}/>
                </div>
            </div>
        ) : null
    );
}
