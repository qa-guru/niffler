import React from 'react';

export const FormInput = ({type, label, fieldName, placeholder, value = "", handleChangeValue, max}) => {

    return (
        <label className="form__label">{label}:
            <input className="form__input"
                   type={type}
                   value={value}
                   name={fieldName}
                   placeholder={placeholder}
                   onChange={handleChangeValue}
                   maxLength={max}
            />
            <span className="form__error" id={`form__${fieldName}-error`}></span>
        </label>
    );
}
