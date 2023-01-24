import React from 'react';

export const FormInput = ({type, label, name, placeholder, value = "", handleChangeValue, max}) => {

    return (
        <label className="form__label">{label}:
            <input className="form__input"
                   type={type}
                   value={value}
                   name={name}
                   placeholder={placeholder}
                   onChange={handleChangeValue}
                   maxLength={max}
            />
            <span className="form__error" id={`form__${name}-error`}></span>
        </label>
    );
}
