export const EditableValue = ({value, onValueChange, fieldName,  placeholder, isEditState}) => {


    const editView = (
        <input
            className="editable__input"
            type={"text"}
            value={value}
            name={fieldName}
            placeholder={placeholder}
            onChange={onValueChange}/>
    );

    const noEditView = (
        <div>
            <div className={"value-container"}>
                <span>{value}</span>
            </div>
        </div>
    )

    return (
        <>
            {
                isEditState ? editView : noEditView
            }
        </>
    );
}
