import React from "react";
import Select from "react-select";

export const FormSelect = ({label, placeholder, options, value, defaultValue, onChange, error, required}) => {
    return (
        <label className="form__label">{label && <>{label} {required && <>*</>}</>}
            <div className="select-wrapper">
                <Select
                    isMulti={false}
                    styles={{
                        input: (baseStyles) => ({
                            ...baseStyles,
                            padding: "4.5px",
                            margin: "0 3px",
                        }),
                        control: (baseStyles, state) => ({
                            ...baseStyles,
                            borderStyle: "solid",
                            borderWidth: "2px",
                            borderColor: error ? "#a10b0b" : state.isFocused ? "blue" : "black",
                            borderRadius: "8px",
                            background: "#eeedea",
                        }),
                        option: (baseStyles) => ({
                            ...baseStyles,
                        }),
                    }}
                    placeholder={placeholder}
                    options={options}
                    value={value ?? null}
                    defaultValue={defaultValue ?? null}
                    onChange={onChange}
                />
            </div>
            {error && (<span className="form__error">{error}</span>)}
        </label>
    )
}
