import dayjs from "dayjs";
import {forwardRef} from "react";
import DatePicker from "react-datepicker";

const CustomInput = forwardRef((props, ref) => (
    <input {...props} id="editable__input" ref={ref}/>
));

export const EditableValue = ({value, onValueChange, fieldName, placeholder, isEditState, type}) => {
    const editView = () => {
        switch (type) {
            case "date": {
                return (
                    <DatePicker
                        dateFormat="dd/MM/yyyy"
                        selected={new Date(value)}
                        onChange={onValueChange}
                        customInput={<CustomInput/>}
                    />
                );
            }
            default: {
                return (<input
                    className="editable__input"
                    type={"text"}
                    value={value}
                    name={fieldName}
                    placeholder={placeholder}
                    onChange={onValueChange}/>);
            }
        }
    };

    const noEditView = (
        <div>
            <div className={"value-container"}>
                <span>{type === "date" ? dayjs(value).format('DD MMM YY') : value}</span>
            </div>
        </div>
    )

    return (
        <>
            {
                isEditState ? editView() : noEditView
            }
        </>
    );
}
