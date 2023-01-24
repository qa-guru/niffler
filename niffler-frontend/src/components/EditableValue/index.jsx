import {useState} from "react";
import {Currency as IconType} from "../../constants/iconType";
import {ButtonIcon} from "../ButtonIcon";

export const EditableValue = ({value, fieldName, label, handleSubmit, placeholder}) => {

    const [editValue, setEditValue] = useState(value);
    const [editState, setEditState] = useState(false);

    const editView = (
        <form className="value-form" onSubmit={handleSubmit}>
            <label className="form__label">{label}:
                <input
                    className="editable__input"
                    type={"text"}
                    value={editValue}
                    name={fieldName}
                    placeholder={placeholder}
                    onChange={(evt) => setEditValue(evt.target.value)}/>
                <span className="form__error" id={`form__${fieldName}-error`}></span>
            </label>
            <ButtonIcon iconType={IconType.CLOSE} onClick={() => setEditState(!editState)}/>
        </form>
    );

    const noEditView = (
        <div>
            <div className="form__label">{label}:</div>
            <div className={"value-container"}>
                <span>{value}</span>
                <ButtonIcon iconType={IconType.EDIT} onClick={() => setEditState(!editState)}/>
            </div>
        </div>
    )

    return (
        <>
            {
                editState ? editView : noEditView
            }
        </>
    );
}
