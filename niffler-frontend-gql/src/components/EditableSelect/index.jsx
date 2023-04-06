import Select from "react-select";

export const EditableSelect = ({value, options, onValueChange, isEditState}) => {
    const editView = (
        <Select
            isMulti={false}
            options={options}
            styles={{
                input: (baseStyles) => ({
                    ...baseStyles,
                    fontFamily: "PTRootUIWeb",
                    textAlign: "center",
                    maxWidth: "100%",
                    width: "fit-content",
                    border: "none",
                    borderBottom: "2px solid black",
                    background: "transparent",
                    display: "block",
                    margin: "3px auto 0",
                }),
                control: (baseStyles) => ({
                    ...baseStyles,
                    border: "none",
                    background: "transparent",
                    outline: "none",
                    minHeight: "none",
                }),
                indicatorsContainer: (baseStyles) => ({
                    ...baseStyles,
                    padding: "0",
                }),
            }}
            value={{value: value, label: value}}
            onChange={onValueChange}
        />
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
