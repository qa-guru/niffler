import React from "react";
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";

export const FormCalendar = ({label, value, onChange, error, required}) => {
    return (
        <label className="form__label">{label && <>{label} {required && <>*</>}</>}
            <div  className={`calendar-wrapper ${error? "calendar-wrapper__error" : ""}`}>
                <DatePicker
                    dateFormat="dd/MM/yyyy"
                    selected={value}
                    onChange={onChange}
                />
            </div>
            {error && (<span className="form__error">{error}</span>)}
        </label>

    );

};
