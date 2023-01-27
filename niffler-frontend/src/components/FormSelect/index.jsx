import Select from "react-select";

export const FormSelect = ({label, placeholder, options, value, defaultValue, onChange}) => {
    return (
        <label className="form__label">{label}
            <div className="select-wrapper">
                <Select
                    isMulti={false}
                    styles={{
                        input: (baseStyles, state) => ({
                            ...baseStyles,
                            padding: "4.5px"
                        }),
                        control: (baseStyles, state) => ({
                            ...baseStyles,
                            borderStyle: "solid",
                            borderBlockWidth: "2px",
                            borderColor: state.isFocused ? "blue" : "black",
                            background: "white",
                            opacity: 0.5,
                        }),
                        option: (baseStyles, state) => ({
                            ...baseStyles,
                            opacity: 0.5,
                        }),
                    }}
                    placeholder={placeholder}
                    options={options}
                    value={value ?? null}
                    defaultValue={defaultValue ?? null}
                    onChange={onChange}
                />
            </div>
        </label>
    )
}
