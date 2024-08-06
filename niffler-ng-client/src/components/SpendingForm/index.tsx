import {Button, Grid, InputLabel, TextField, Typography, useTheme} from "@mui/material";
import {ChangeEvent, FC, FormEvent, useEffect, useState} from "react";
import {CategorySelect} from "../CategorySelect";
import {CurrencySelect} from "../CurrencySelect";
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import {LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";
import dayjs from "dayjs";
import {CalendarIcon} from "../Icon/CalendarIcon.tsx";
import {apiClient} from "../../api/apiClient.ts";
import {useNavigate} from "react-router-dom";
import {useSnackBar} from "../../context/SnackBarContext.tsx";


const INITIAL_STATE = {
    amount: 0,
    description: "",
    currency: "RUB",
    spendDate: dayjs(new Date()),
    category: "",
}

interface SpendingFormInterface {
    id?: string,
    isEdit: boolean,
}

export const SpendingForm: FC<SpendingFormInterface> = ({id, isEdit}) => {

    const theme = useTheme();
    const navigate = useNavigate();
    const snackbar = useSnackBar();

    const [formData, setFormData] = useState(INITIAL_STATE);

    useEffect(() => {
        if(isEdit && id) {
            apiClient.getSpend(id, {
                onSuccess: data => setFormData({
                    amount: data.amount,
                    description: data.description,
                    currency: data.currency,
                    spendDate: dayjs(data.spendDate),
                    category: data.category.name,
                }),
                onFailure: e => {
                    setFormData(INITIAL_STATE);
                    console.log(e);
                },
            });
        } else {
            setFormData(INITIAL_STATE);
        }
    }, [id]);

    const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
        setFormData({
            ...formData,
            [event.target.name]: event.target.value,
        });
    };

    const onAdd = (e: FormEvent) => {
        e.preventDefault();
        const data = {
            amount: formData.amount,
            description: formData.description,
            currency: formData.currency,
            spendDate: formData.spendDate.toISOString(),
            category: {
                category: formData.category,
            }
        };
        apiClient.addSpend(data, {
            onSuccess: (_data) => {
                snackbar.showSnackBar("New spending is successfully created", "success");
                navigate("/main");
            },
            onFailure: e => console.log(e),
        });
    };

    const onEdit = (e: FormEvent) => {
        e.preventDefault();
        const data = {
            id,
            amount: formData.amount,
            description: formData.description,
            currency: formData.currency,
            spendDate: formData.spendDate.toISOString(),
            category: {
                category: formData.category,
            }
        };
        apiClient.editSpend(data, {
            onSuccess: (_data) => {
                snackbar.showSnackBar(`Spending is edited successfully`, "success");
                navigate("/main");
            },
            onFailure: e => console.log(e),
        });
    };



    const onCancel = () => {
        navigate(-1);
    }


    return (
        <Grid
            container
            component="form"
            onSubmit={isEdit ? onEdit : onAdd}
            spacing={2}
            sx={{
                maxWidth: "320px",
                margin: "40px auto"
            }}
        >
            <Grid item xs={12}>
                <Typography
                    variant="h5"
                    component="h2"
                >
                    Add new spending
                </Typography>
            </Grid>
            <Grid item xs={12}>
                <Grid
                    container
                    spacing={3}
                >
                    <Grid item xs={8} sx={{width: "100%"}}>
                        <InputLabel
                            htmlFor={"amount"}
                            sx={{
                                color: theme.palette.black.main,
                            }}>
                            Amount
                        </InputLabel>
                        <TextField
                            sx={{
                                margin: 0,
                                padding: 0
                            }}
                            id="amount"
                            name="amount"
                            type="number"
                            value={formData.amount}
                            error={false}
                            onChange={handleChange}
                            helperText={""}
                            fullWidth
                        />
                    </Grid>
                    <Grid item xs={4} sx={{width: "100%"}}>
                        <InputLabel
                            htmlFor={"currency"}
                            sx={{
                                color: theme.palette.black.main,
                            }}>
                            Currency
                        </InputLabel>
                       <CurrencySelect
                           selectedCurrency={formData.currency}
                           onCurrencyChange={handleChange}/>
                    </Grid>

                </Grid>
            </Grid>
            <Grid item xs={12}>
                <InputLabel
                    htmlFor={"date"}
                    sx={{
                        color: theme.palette.black.main,
                    }}>
                    Category
                </InputLabel>
                <CategorySelect  selectedCategory={formData.category} onSelectCategory={(category) => setFormData({...formData, category}) }/>
            </Grid>
            <Grid item xs={12}>
                <InputLabel
                    htmlFor={"date"}
                    sx={{
                        color: theme.palette.black.main,
                    }}>
                    Date
                </InputLabel>
                <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <DatePicker
                        sx={{
                            width: "100%",
                        }}
                        name={"date"}
                        slots={{
                            openPickerIcon: CalendarIcon,
                        }}
                        value={formData.spendDate}
                        onChange={newDate => setFormData({...formData, spendDate: dayjs(newDate)})}
                    />
                </LocalizationProvider>
            </Grid>
            <Grid item xs={12}>
                <InputLabel
                    htmlFor={"description"}
                    sx={{
                        color: theme.palette.black.main,
                    }}>
                    Description
                </InputLabel>
                <TextField
                    sx={{
                        margin: 0,
                        padding: 0
                    }}
                    id="description"
                    name="description"
                    type="text"
                    value={formData.description}
                    error={false}
                    helperText={""}
                    onChange={handleChange}
                    placeholder={"Type something"}
                    fullWidth
                />
            </Grid>
            <Grid item xs={12} sx={{
                marginTop: 2,
                width: "100%",
                display: "flex",
            }}>
                <Button type="button"
                        sx={{
                            marginRight: 3,
                            width: "100%",
                        }}
                        color="secondary"
                        variant="contained"
                        onClick={onCancel}
                >
                    Cancel
                </Button>
                <Button
                    sx={{
                        width: "100%",
                    }}
                    type="submit"
                    color="primary"
                    variant="contained"
                >
                    {isEdit ? "Save changes" : "Add"}

                </Button>
            </Grid>
        </Grid>
    )
}