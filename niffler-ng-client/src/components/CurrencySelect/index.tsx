import {ChangeEvent, FC, useEffect, useState} from "react";
import {Currency, CurrencyValue, getCurrencyIcon} from "../../types/Currency.ts";
import {apiClient} from "../../api/apiClient.ts";
import {MenuItem, Stack, TextField, useTheme} from "@mui/material";

interface CurrencySelectInterface {
    selectedCurrency: string,
    onCurrencyChange: (e: ChangeEvent<HTMLInputElement>) => void;
}

export const CurrencySelect: FC<CurrencySelectInterface> = ({selectedCurrency, onCurrencyChange}) => {
    const [currencies, setCurrencies] = useState<Currency[]>([]);
    const theme = useTheme();

    useEffect(() => {
        apiClient.getCurrencies({
            onSuccess: (data) => {
                setCurrencies(data);
            },
            onFailure: (e) => console.log(e),
        });
    }, []);

    return (
        currencies.length > 0 &&
        <TextField
            sx={{
                padding: 0,
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
                    <Stack sx={{fontSize: 18, display: "inline"}} component="span">
                        {getCurrencyIcon(option.currency as CurrencyValue)}
                    </Stack>
                    &nbsp;&nbsp;
                    <Stack sx={{color: theme.palette.gray_600.main, display: "inline"}} component="span">
                        {option.currency}
                    </Stack>
                </MenuItem>
            ))}
        </TextField>
    );
}
