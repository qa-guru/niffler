import * as React from "react";
import {ChangeEvent, FC} from "react";
import "./style.css";

interface InputInterface {
    type: "text" | "number",
    value: string | number | readonly string[] | undefined,
    name: string,
    placeholder?: string,
    error?: boolean,
    helperText?: string,
    id?: string,
    disabled?: boolean,
    onChange?: (event: ChangeEvent<HTMLInputElement>) => void,
    onKeyDown?: (event: React.KeyboardEvent<HTMLInputElement>) => void,
}

export const Input: FC<InputInterface> = ({
                                              type,
                                              value,
                                              onChange,
                                              name,
                                              error,
                                              helperText,
                                              id,
                                              placeholder,
                                              onKeyDown,
                                              disabled = false
                                          }) => {
    const className = `custom-input ${error && "custom-input--error"}`.trim();
    return (
        <>
            <input
                type={type}
                value={value}
                onChange={onChange}
                onKeyDown={onKeyDown}
                name={name}
                id={id}
                placeholder={placeholder}
                className={className}
                disabled={disabled}
            />
            {helperText && <span className={"input__helper-text"}>{helperText}</span>}
        </>

    );
}