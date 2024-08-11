import {ChangeEvent, FC, useEffect, useState} from "react";
import {Currency} from "../../types/Currency.ts";
import {apiClient} from "../../api/apiClient.ts";
import {MenuItem, TextField} from "@mui/material";

interface CurrencySelectInterface {
    selectedCurrency: string,
    onCurrencyChange:(e: ChangeEvent<HTMLInputElement>) => void;
}

export const CurrencySelect: FC<CurrencySelectInterface> = ({selectedCurrency, onCurrencyChange}) => {
    const [currencies, setCurrencies] = useState<Currency[]>([]);

    useEffect(() => {
        apiClient.getCurrencies({
            onSuccess: (data) => {
                setCurrencies(data);
            },
            onFailure: (e) => console.log(e),
        });
    }, []);

    return (
        <TextField
            sx={{
                margin: "0 8px",
                padding: 0,
                maxWidth: "100px"
            }}
            id="currency"
            name="currency"
            type="text"
            select
            defaultValue={"RUB"}
            error={false}
            fullWidth
            value={selectedCurrency}
            onChange={onCurrencyChange}
        >
            {currencies.map((option) => (
                <MenuItem key={option.currency} value={option.currency}>
                    {option.currency}
                </MenuItem>
            ))}
        </TextField>
    );
}
