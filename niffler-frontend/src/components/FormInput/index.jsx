import React from 'react';

export const FormInput = ({
                              type,
                              label,
                              fieldName,
                              placeholder,
                              value = "",
                              handleChangeValue,
                              max,
                              error,
                              required
}) => {

    return (
        <label className="form__label">{label && <>{label} {required && <>*</>}</>}
            <input className={`form__input ${error? "form__input-error" : ""}`}
                   type={type}
                   value={value}
                   name={fieldName}
                   placeholder={placeholder}
                   onChange={handleChangeValue}
                   maxLength={max}
            />
            {error && (<span className="form__error">{error}</span>)}
        </label>
    );
}
